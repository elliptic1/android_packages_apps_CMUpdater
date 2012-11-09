/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/7af6e9e4-35a3-cd01-40a6-e1e435a3cd01/android/system/frameworks/base/core/java/android/view/accessibility/IAccessibilityInteractionConnection.aidl
 */
package android.view.accessibility;
/**
 * Interface for interaction between the AccessibilityManagerService
 * and the ViewRoot in a given window.
 *
 * @hide
 */
public interface IAccessibilityInteractionConnection extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.view.accessibility.IAccessibilityInteractionConnection
{
private static final java.lang.String DESCRIPTOR = "android.view.accessibility.IAccessibilityInteractionConnection";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.view.accessibility.IAccessibilityInteractionConnection interface,
 * generating a proxy if needed.
 */
public static android.view.accessibility.IAccessibilityInteractionConnection asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.view.accessibility.IAccessibilityInteractionConnection))) {
return ((android.view.accessibility.IAccessibilityInteractionConnection)iin);
}
return new android.view.accessibility.IAccessibilityInteractionConnection.Stub.Proxy(obj);
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
case TRANSACTION_findAccessibilityNodeInfoByAccessibilityId:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg4;
_arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
int _arg5;
_arg5 = data.readInt();
int _arg6;
_arg6 = data.readInt();
long _arg7;
_arg7 = data.readLong();
this.findAccessibilityNodeInfoByAccessibilityId(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
return true;
}
case TRANSACTION_findAccessibilityNodeInfoByViewId:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg5;
_arg5 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
int _arg6;
_arg6 = data.readInt();
int _arg7;
_arg7 = data.readInt();
long _arg8;
_arg8 = data.readLong();
this.findAccessibilityNodeInfoByViewId(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
return true;
}
case TRANSACTION_findAccessibilityNodeInfosByText:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg5;
_arg5 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
int _arg6;
_arg6 = data.readInt();
int _arg7;
_arg7 = data.readInt();
long _arg8;
_arg8 = data.readLong();
this.findAccessibilityNodeInfosByText(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
return true;
}
case TRANSACTION_findFocus:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg5;
_arg5 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
int _arg6;
_arg6 = data.readInt();
int _arg7;
_arg7 = data.readInt();
long _arg8;
_arg8 = data.readLong();
this.findFocus(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
return true;
}
case TRANSACTION_focusSearch:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg5;
_arg5 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
int _arg6;
_arg6 = data.readInt();
int _arg7;
_arg7 = data.readInt();
long _arg8;
_arg8 = data.readLong();
this.focusSearch(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
return true;
}
case TRANSACTION_performAccessibilityAction:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
int _arg1;
_arg1 = data.readInt();
android.os.Bundle _arg2;
if ((0!=data.readInt())) {
_arg2 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
int _arg3;
_arg3 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg4;
_arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
int _arg5;
_arg5 = data.readInt();
int _arg6;
_arg6 = data.readInt();
long _arg7;
_arg7 = data.readLong();
this.performAccessibilityAction(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.view.accessibility.IAccessibilityInteractionConnection
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
public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(accessibilityNodeId);
_data.writeInt(windowLeft);
_data.writeInt(windowTop);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(flags);
_data.writeInt(interrogatingPid);
_data.writeLong(interrogatingTid);
mRemote.transact(Stub.TRANSACTION_findAccessibilityNodeInfoByAccessibilityId, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
public void findAccessibilityNodeInfoByViewId(long accessibilityNodeId, int viewId, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(accessibilityNodeId);
_data.writeInt(viewId);
_data.writeInt(windowLeft);
_data.writeInt(windowTop);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(flags);
_data.writeInt(interrogatingPid);
_data.writeLong(interrogatingTid);
mRemote.transact(Stub.TRANSACTION_findAccessibilityNodeInfoByViewId, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
public void findAccessibilityNodeInfosByText(long accessibilityNodeId, java.lang.String text, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(accessibilityNodeId);
_data.writeString(text);
_data.writeInt(windowLeft);
_data.writeInt(windowTop);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(flags);
_data.writeInt(interrogatingPid);
_data.writeLong(interrogatingTid);
mRemote.transact(Stub.TRANSACTION_findAccessibilityNodeInfosByText, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
public void findFocus(long accessibilityNodeId, int focusType, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(accessibilityNodeId);
_data.writeInt(focusType);
_data.writeInt(windowLeft);
_data.writeInt(windowTop);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(flags);
_data.writeInt(interrogatingPid);
_data.writeLong(interrogatingTid);
mRemote.transact(Stub.TRANSACTION_findFocus, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
public void focusSearch(long accessibilityNodeId, int direction, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(accessibilityNodeId);
_data.writeInt(direction);
_data.writeInt(windowLeft);
_data.writeInt(windowTop);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(flags);
_data.writeInt(interrogatingPid);
_data.writeLong(interrogatingTid);
mRemote.transact(Stub.TRANSACTION_focusSearch, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
public void performAccessibilityAction(long accessibilityNodeId, int action, android.os.Bundle arguments, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(accessibilityNodeId);
_data.writeInt(action);
if ((arguments!=null)) {
_data.writeInt(1);
arguments.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(flags);
_data.writeInt(interrogatingPid);
_data.writeLong(interrogatingTid);
mRemote.transact(Stub.TRANSACTION_performAccessibilityAction, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_findAccessibilityNodeInfoByAccessibilityId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_findAccessibilityNodeInfoByViewId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_findAccessibilityNodeInfosByText = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_findFocus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_focusSearch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_performAccessibilityAction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException;
public void findAccessibilityNodeInfoByViewId(long accessibilityNodeId, int viewId, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException;
public void findAccessibilityNodeInfosByText(long accessibilityNodeId, java.lang.String text, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException;
public void findFocus(long accessibilityNodeId, int focusType, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException;
public void focusSearch(long accessibilityNodeId, int direction, int windowLeft, int windowTop, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException;
public void performAccessibilityAction(long accessibilityNodeId, int action, android.os.Bundle arguments, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws android.os.RemoteException;
}
