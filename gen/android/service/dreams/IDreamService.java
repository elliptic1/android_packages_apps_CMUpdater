/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/7af6e9e4-35a3-cd01-40a6-e1e435a3cd01/android/system/frameworks/base/core/java/android/service/dreams/IDreamService.aidl
 */
package android.service.dreams;
/**
 * @hide
 */
public interface IDreamService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.service.dreams.IDreamService
{
private static final java.lang.String DESCRIPTOR = "android.service.dreams.IDreamService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.service.dreams.IDreamService interface,
 * generating a proxy if needed.
 */
public static android.service.dreams.IDreamService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.service.dreams.IDreamService))) {
return ((android.service.dreams.IDreamService)iin);
}
return new android.service.dreams.IDreamService.Stub.Proxy(obj);
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
case TRANSACTION_attach:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
this.attach(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.service.dreams.IDreamService
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
public void attach(android.os.IBinder windowToken) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(windowToken);
mRemote.transact(Stub.TRANSACTION_attach, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_attach = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void attach(android.os.IBinder windowToken) throws android.os.RemoteException;
}
