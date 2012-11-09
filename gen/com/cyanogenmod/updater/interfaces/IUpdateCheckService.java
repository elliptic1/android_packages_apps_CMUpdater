/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/7af6e9e4-35a3-cd01-40a6-e1e435a3cd01/CMUpdater/src/com/cyanogenmod/updater/interfaces/IUpdateCheckService.aidl
 */
package com.cyanogenmod.updater.interfaces;
public interface IUpdateCheckService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.cyanogenmod.updater.interfaces.IUpdateCheckService
{
private static final java.lang.String DESCRIPTOR = "com.cyanogenmod.updater.interfaces.IUpdateCheckService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.cyanogenmod.updater.interfaces.IUpdateCheckService interface,
 * generating a proxy if needed.
 */
public static com.cyanogenmod.updater.interfaces.IUpdateCheckService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.cyanogenmod.updater.interfaces.IUpdateCheckService))) {
return ((com.cyanogenmod.updater.interfaces.IUpdateCheckService)iin);
}
return new com.cyanogenmod.updater.interfaces.IUpdateCheckService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_checkForUpdates:
{
data.enforceInterface(DESCRIPTOR);
this.checkForUpdates();
reply.writeNoException();
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback _arg0;
_arg0 = com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback _arg0;
_arg0 = com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.cyanogenmod.updater.interfaces.IUpdateCheckService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void checkForUpdates() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_checkForUpdates, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void registerCallback(com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void unregisterCallback(com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_checkForUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void checkForUpdates() throws android.os.RemoteException;
public void registerCallback(com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback cb) throws android.os.RemoteException;
public void unregisterCallback(com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback cb) throws android.os.RemoteException;
}
