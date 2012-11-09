/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/7af6e9e4-35a3-cd01-40a6-e1e435a3cd01/CMUpdater/src/com/cyanogenmod/updater/interfaces/IUpdateCheckServiceCallback.aidl
 */
package com.cyanogenmod.updater.interfaces;
public interface IUpdateCheckServiceCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback
{
private static final java.lang.String DESCRIPTOR = "com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback interface,
 * generating a proxy if needed.
 */
public static com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback))) {
return ((com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback)iin);
}
return new com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback.Stub.Proxy(obj);
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
case TRANSACTION_updateCheckFinished:
{
data.enforceInterface(DESCRIPTOR);
this.updateCheckFinished();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.cyanogenmod.updater.interfaces.IUpdateCheckServiceCallback
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
public void updateCheckFinished() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateCheckFinished, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_updateCheckFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void updateCheckFinished() throws android.os.RemoteException;
}
