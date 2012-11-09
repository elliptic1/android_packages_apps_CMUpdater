/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/7af6e9e4-35a3-cd01-40a6-e1e435a3cd01/android/system/frameworks/base/core/java/android/accessibilityservice/IAccessibilityServiceConnection.aidl
 */
package android.accessibilityservice;
/**
 * Interface given to an AccessibilitySerivce to talk to the AccessibilityManagerService.
 *
 * @hide
 */
public interface IAccessibilityServiceConnection extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.accessibilityservice.IAccessibilityServiceConnection
{
private static final java.lang.String DESCRIPTOR = "android.accessibilityservice.IAccessibilityServiceConnection";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.accessibilityservice.IAccessibilityServiceConnection interface,
 * generating a proxy if needed.
 */
public static android.accessibilityservice.IAccessibilityServiceConnection asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.accessibilityservice.IAccessibilityServiceConnection))) {
return ((android.accessibilityservice.IAccessibilityServiceConnection)iin);
}
return new android.accessibilityservice.IAccessibilityServiceConnection.Stub.Proxy(obj);
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
case TRANSACTION_setServiceInfo:
{
data.enforceInterface(DESCRIPTOR);
android.accessibilityservice.AccessibilityServiceInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = android.accessibilityservice.AccessibilityServiceInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.setServiceInfo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_findAccessibilityNodeInfoByAccessibilityId:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
int _arg2;
_arg2 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg3;
_arg3 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
int _arg4;
_arg4 = data.readInt();
long _arg5;
_arg5 = data.readLong();
float _result = this.findAccessibilityNodeInfoByAccessibilityId(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_findAccessibilityNodeInfosByText:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg4;
_arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
long _arg5;
_arg5 = data.readLong();
float _result = this.findAccessibilityNodeInfosByText(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_findAccessibilityNodeInfoByViewId:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg4;
_arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
long _arg5;
_arg5 = data.readLong();
float _result = this.findAccessibilityNodeInfoByViewId(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_findFocus:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg4;
_arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
long _arg5;
_arg5 = data.readLong();
float _result = this.findFocus(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_focusSearch:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg4;
_arg4 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
long _arg5;
_arg5 = data.readLong();
float _result = this.focusSearch(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_performAccessibilityAction:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
int _arg2;
_arg2 = data.readInt();
android.os.Bundle _arg3;
if ((0!=data.readInt())) {
_arg3 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
int _arg4;
_arg4 = data.readInt();
android.view.accessibility.IAccessibilityInteractionConnectionCallback _arg5;
_arg5 = android.view.accessibility.IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
long _arg6;
_arg6 = data.readLong();
boolean _result = this.performAccessibilityAction(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getServiceInfo:
{
data.enforceInterface(DESCRIPTOR);
android.accessibilityservice.AccessibilityServiceInfo _result = this.getServiceInfo();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_performGlobalAction:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.performGlobalAction(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.accessibilityservice.IAccessibilityServiceConnection
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
public void setServiceInfo(android.accessibilityservice.AccessibilityServiceInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_setServiceInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} by accessibility id.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param flags Additional flags.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId, long accessibilityNodeId, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, long threadId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(accessibilityWindowId);
_data.writeLong(accessibilityNodeId);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeInt(flags);
_data.writeLong(threadId);
mRemote.transact(Stub.TRANSACTION_findAccessibilityNodeInfoByAccessibilityId, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Finds {@link android.view.accessibility.AccessibilityNodeInfo}s by View text.
     * The match is case insensitive containment. The search is performed in the window
     * whose id is specified and starts from the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param text The searched text.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findAccessibilityNodeInfosByText(int accessibilityWindowId, long accessibilityNodeId, java.lang.String text, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(accessibilityWindowId);
_data.writeLong(accessibilityNodeId);
_data.writeString(text);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeLong(threadId);
mRemote.transact(Stub.TRANSACTION_findAccessibilityNodeInfosByText, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} by View id. The search
     * is performed in the window whose id is specified and starts from the node whose
     * accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param id The id of the node.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findAccessibilityNodeInfoByViewId(int accessibilityWindowId, long accessibilityNodeId, int viewId, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(accessibilityWindowId);
_data.writeLong(accessibilityNodeId);
_data.writeInt(viewId);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeLong(threadId);
mRemote.transact(Stub.TRANSACTION_findAccessibilityNodeInfoByViewId, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Finds the {@link android.view.accessibility.AccessibilityNodeInfo} that has the specified
     * focus type. The search is performed in the window whose id is specified and starts from
     * the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param focusType The type of focus to find.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findFocus(int accessibilityWindowId, long accessibilityNodeId, int focusType, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(accessibilityWindowId);
_data.writeLong(accessibilityNodeId);
_data.writeInt(focusType);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeLong(threadId);
mRemote.transact(Stub.TRANSACTION_findFocus, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} to take accessibility
     * focus in the given direction. The search is performed in the window whose id is
     * specified and starts from the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param direction The direction in which to search for focusable.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float focusSearch(int accessibilityWindowId, long accessibilityNodeId, int direction, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(accessibilityWindowId);
_data.writeLong(accessibilityNodeId);
_data.writeInt(direction);
_data.writeInt(interactionId);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeLong(threadId);
mRemote.transact(Stub.TRANSACTION_focusSearch, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Performs an accessibility action on an
     * {@link android.view.accessibility.AccessibilityNodeInfo}.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param action The action to perform.
     * @param arguments Optional action arguments.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return Whether the action was performed.
     */
public boolean performAccessibilityAction(int accessibilityWindowId, long accessibilityNodeId, int action, android.os.Bundle arguments, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(accessibilityWindowId);
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
_data.writeLong(threadId);
mRemote.transact(Stub.TRANSACTION_performAccessibilityAction, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * @return The associated accessibility service info.
     */
public android.accessibilityservice.AccessibilityServiceInfo getServiceInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.accessibilityservice.AccessibilityServiceInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getServiceInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.accessibilityservice.AccessibilityServiceInfo.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Performs a global action, such as going home, going back, etc.
     *
     * @param action The action to perform.
     * @return Whether the action was performed.
     */
public boolean performGlobalAction(int action) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(action);
mRemote.transact(Stub.TRANSACTION_performGlobalAction, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setServiceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_findAccessibilityNodeInfoByAccessibilityId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_findAccessibilityNodeInfosByText = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_findAccessibilityNodeInfoByViewId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_findFocus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_focusSearch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_performAccessibilityAction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getServiceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_performGlobalAction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
}
public void setServiceInfo(android.accessibilityservice.AccessibilityServiceInfo info) throws android.os.RemoteException;
/**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} by accessibility id.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param flags Additional flags.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findAccessibilityNodeInfoByAccessibilityId(int accessibilityWindowId, long accessibilityNodeId, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, int flags, long threadId) throws android.os.RemoteException;
/**
     * Finds {@link android.view.accessibility.AccessibilityNodeInfo}s by View text.
     * The match is case insensitive containment. The search is performed in the window
     * whose id is specified and starts from the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param text The searched text.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findAccessibilityNodeInfosByText(int accessibilityWindowId, long accessibilityNodeId, java.lang.String text, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException;
/**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} by View id. The search
     * is performed in the window whose id is specified and starts from the node whose
     * accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param id The id of the node.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findAccessibilityNodeInfoByViewId(int accessibilityWindowId, long accessibilityNodeId, int viewId, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException;
/**
     * Finds the {@link android.view.accessibility.AccessibilityNodeInfo} that has the specified
     * focus type. The search is performed in the window whose id is specified and starts from
     * the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param focusType The type of focus to find.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float findFocus(int accessibilityWindowId, long accessibilityNodeId, int focusType, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException;
/**
     * Finds an {@link android.view.accessibility.AccessibilityNodeInfo} to take accessibility
     * focus in the given direction. The search is performed in the window whose id is
     * specified and starts from the node whose accessibility id is specified.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param direction The direction in which to search for focusable.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return The current window scale, where zero means a failure.
     */
public float focusSearch(int accessibilityWindowId, long accessibilityNodeId, int direction, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException;
/**
     * Performs an accessibility action on an
     * {@link android.view.accessibility.AccessibilityNodeInfo}.
     *
     * @param accessibilityWindowId A unique window id. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ACTIVE_WINDOW_ID}
     *     to query the currently active window.
     * @param accessibilityNodeId A unique view id or virtual descendant id from
     *     where to start the search. Use
     *     {@link android.view.accessibility.AccessibilityNodeInfo#ROOT_NODE_ID}
     *     to start from the root.
     * @param action The action to perform.
     * @param arguments Optional action arguments.
     * @param interactionId The id of the interaction for matching with the callback result.
     * @param callback Callback which to receive the result.
     * @param threadId The id of the calling thread.
     * @return Whether the action was performed.
     */
public boolean performAccessibilityAction(int accessibilityWindowId, long accessibilityNodeId, int action, android.os.Bundle arguments, int interactionId, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback, long threadId) throws android.os.RemoteException;
/**
     * @return The associated accessibility service info.
     */
public android.accessibilityservice.AccessibilityServiceInfo getServiceInfo() throws android.os.RemoteException;
/**
     * Performs a global action, such as going home, going back, etc.
     *
     * @param action The action to perform.
     * @return Whether the action was performed.
     */
public boolean performGlobalAction(int action) throws android.os.RemoteException;
}
