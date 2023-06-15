package io.rotlabs.flakerretrofit

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import android.view.Display
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class FakeContext: Context() {
    override fun getAssets(): AssetManager {
        TODO("Fake Context. No implementation")
    }

    override fun getResources(): Resources {
        TODO("Fake Context. No implementation")
    }

    override fun getPackageManager(): PackageManager {
        TODO("Fake Context. No implementation")
    }

    override fun getContentResolver(): ContentResolver {
        TODO("Fake Context. No implementation")
    }

    override fun getMainLooper(): Looper {
        TODO("Fake Context. No implementation")
    }

    override fun getApplicationContext(): Context {
        TODO("Fake Context. No implementation")
    }

    override fun setTheme(resid: Int) {
        TODO("Fake Context. No implementation")
    }

    override fun getTheme(): Resources.Theme {
        TODO("Fake Context. No implementation")
    }

    override fun getClassLoader(): ClassLoader {
        TODO("Fake Context. No implementation")
    }

    override fun getPackageName(): String {
        TODO("Fake Context. No implementation")
    }

    override fun getApplicationInfo(): ApplicationInfo {
        TODO("Fake Context. No implementation")
    }

    override fun getPackageResourcePath(): String {
        TODO("Fake Context. No implementation")
    }

    override fun getPackageCodePath(): String {
        TODO("Fake Context. No implementation")
    }

    override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
        TODO("Fake Context. No implementation")
    }

    override fun moveSharedPreferencesFrom(sourceContext: Context?, name: String?): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun deleteSharedPreferences(name: String?): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun openFileInput(name: String?): FileInputStream {
        TODO("Fake Context. No implementation")
    }

    override fun openFileOutput(name: String?, mode: Int): FileOutputStream {
        TODO("Fake Context. No implementation")
    }

    override fun deleteFile(name: String?): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun getFileStreamPath(name: String?): File {
        TODO("Fake Context. No implementation")
    }

    override fun getDataDir(): File {
        TODO("Fake Context. No implementation")
    }

    override fun getFilesDir(): File {
        TODO("Fake Context. No implementation")
    }

    override fun getNoBackupFilesDir(): File {
        TODO("Fake Context. No implementation")
    }

    override fun getExternalFilesDir(type: String?): File? {
        TODO("Fake Context. No implementation")
    }

    override fun getExternalFilesDirs(type: String?): Array<File> {
        TODO("Fake Context. No implementation")
    }

    override fun getObbDir(): File {
        TODO("Fake Context. No implementation")
    }

    override fun getObbDirs(): Array<File> {
        TODO("Fake Context. No implementation")
    }

    override fun getCacheDir(): File {
        TODO("Fake Context. No implementation")
    }

    override fun getCodeCacheDir(): File {
        TODO("Fake Context. No implementation")
    }

    override fun getExternalCacheDir(): File? {
        TODO("Fake Context. No implementation")
    }

    override fun getExternalCacheDirs(): Array<File> {
        TODO("Fake Context. No implementation")
    }

    override fun getExternalMediaDirs(): Array<File> {
        TODO("Fake Context. No implementation")
    }

    override fun fileList(): Array<String> {
        TODO("Fake Context. No implementation")
    }

    override fun getDir(name: String?, mode: Int): File {
        TODO("Fake Context. No implementation")
    }

    override fun openOrCreateDatabase(name: String?, mode: Int, factory: SQLiteDatabase.CursorFactory?): SQLiteDatabase {
        TODO("Fake Context. No implementation")
    }

    override fun openOrCreateDatabase(name: String?, mode: Int, factory: SQLiteDatabase.CursorFactory?, errorHandler: DatabaseErrorHandler?): SQLiteDatabase {
        TODO("Fake Context. No implementation")
    }

    override fun moveDatabaseFrom(sourceContext: Context?, name: String?): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun deleteDatabase(name: String?): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun getDatabasePath(name: String?): File {
        TODO("Fake Context. No implementation")
    }

    override fun databaseList(): Array<String> {
        TODO("Fake Context. No implementation")
    }

    override fun getWallpaper(): Drawable {
        TODO("Fake Context. No implementation")
    }

    override fun peekWallpaper(): Drawable {
        TODO("Fake Context. No implementation")
    }

    override fun getWallpaperDesiredMinimumWidth(): Int {
        TODO("Fake Context. No implementation")
    }

    override fun getWallpaperDesiredMinimumHeight(): Int {
        TODO("Fake Context. No implementation")
    }

    override fun setWallpaper(bitmap: Bitmap?) {
        TODO("Fake Context. No implementation")
    }

    override fun setWallpaper(data: InputStream?) {
        TODO("Fake Context. No implementation")
    }

    override fun clearWallpaper() {
        TODO("Fake Context. No implementation")
    }

    override fun startActivity(intent: Intent?) {
        TODO("Fake Context. No implementation")
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        TODO("Fake Context. No implementation")
    }

    override fun startActivities(intents: Array<out Intent>?) {
        TODO("Fake Context. No implementation")
    }

    override fun startActivities(intents: Array<out Intent>?, options: Bundle?) {
        TODO("Fake Context. No implementation")
    }

    override fun startIntentSender(intent: IntentSender?, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int) {
        TODO("Fake Context. No implementation")
    }

    override fun startIntentSender(intent: IntentSender?, fillInIntent: Intent?, flagsMask: Int, flagsValues: Int, extraFlags: Int, options: Bundle?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendBroadcast(intent: Intent?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendBroadcast(intent: Intent?, receiverPermission: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendOrderedBroadcast(intent: Intent?, receiverPermission: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendOrderedBroadcast(intent: Intent, receiverPermission: String?, resultReceiver: BroadcastReceiver?, scheduler: Handler?, initialCode: Int,
                                      initialData: String?, initialExtras: Bundle?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendBroadcastAsUser(intent: Intent?, user: UserHandle?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendBroadcastAsUser(intent: Intent?, user: UserHandle?, receiverPermission: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendOrderedBroadcastAsUser(intent: Intent?, user: UserHandle?, receiverPermission: String?, resultReceiver: BroadcastReceiver?,
                                            scheduler: Handler?, initialCode: Int, initialData: String?, initialExtras: Bundle?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendStickyBroadcast(intent: Intent?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendStickyOrderedBroadcast(intent: Intent?, resultReceiver: BroadcastReceiver?, scheduler: Handler?, initialCode: Int, initialData: String?,
                                            initialExtras: Bundle?) {
        TODO("Fake Context. No implementation")
    }

    override fun removeStickyBroadcast(intent: Intent?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendStickyBroadcastAsUser(intent: Intent?, user: UserHandle?) {
        TODO("Fake Context. No implementation")
    }

    override fun sendStickyOrderedBroadcastAsUser(intent: Intent?, user: UserHandle?, resultReceiver: BroadcastReceiver?, scheduler: Handler?, initialCode: Int,
                                                  initialData: String?, initialExtras: Bundle?) {
        TODO("Fake Context. No implementation")
    }

    override fun removeStickyBroadcastAsUser(intent: Intent?, user: UserHandle?) {
        TODO("Fake Context. No implementation")
    }

    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?): Intent? {
        TODO("Fake Context. No implementation")
    }

    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?, flags: Int): Intent? {
        TODO("Fake Context. No implementation")
    }

    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?, broadcastPermission: String?, scheduler: Handler?): Intent? {
        TODO("Fake Context. No implementation")
    }

    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?, broadcastPermission: String?, scheduler: Handler?, flags: Int): Intent? {
        TODO("Fake Context. No implementation")
    }

    override fun unregisterReceiver(receiver: BroadcastReceiver?) {
        TODO("Fake Context. No implementation")
    }

    override fun startService(service: Intent?): ComponentName? {
        TODO("Fake Context. No implementation")
    }

    override fun startForegroundService(service: Intent?): ComponentName? {
        TODO("Fake Context. No implementation")
    }

    override fun stopService(service: Intent?): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun unbindService(conn: ServiceConnection) {
        TODO("Fake Context. No implementation")
    }

    override fun startInstrumentation(className: ComponentName, profileFile: String?, arguments: Bundle?): Boolean {
        TODO("Fake Context. No implementation")
    }

    override fun getSystemService(name: String): Any {
        TODO("Fake Context. No implementation")
    }

    override fun getSystemServiceName(serviceClass: Class<*>): String? {
        TODO("Fake Context. No implementation")
    }

    override fun checkPermission(permission: String, pid: Int, uid: Int): Int {
        TODO("Fake Context. No implementation")
    }

    override fun checkCallingPermission(permission: String): Int {
        TODO("Fake Context. No implementation")
    }

    override fun checkCallingOrSelfPermission(permission: String): Int {
        TODO("Fake Context. No implementation")
    }

    override fun checkSelfPermission(permission: String): Int {
        TODO("Fake Context. No implementation")
    }

    override fun enforcePermission(permission: String, pid: Int, uid: Int, message: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun enforceCallingPermission(permission: String, message: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun enforceCallingOrSelfPermission(permission: String, message: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun grantUriPermission(toPackage: String?, uri: Uri?, modeFlags: Int) {
        TODO("Fake Context. No implementation")
    }

    override fun revokeUriPermission(uri: Uri?, modeFlags: Int) {
        TODO("Fake Context. No implementation")
    }

    override fun revokeUriPermission(toPackage: String?, uri: Uri?, modeFlags: Int) {
        TODO("Fake Context. No implementation")
    }

    override fun checkUriPermission(uri: Uri?, pid: Int, uid: Int, modeFlags: Int): Int {
        TODO("Fake Context. No implementation")
    }

    override fun checkUriPermission(uri: Uri?, readPermission: String?, writePermission: String?, pid: Int, uid: Int, modeFlags: Int): Int {
        TODO("Fake Context. No implementation")
    }

    override fun checkCallingUriPermission(uri: Uri?, modeFlags: Int): Int {
        TODO("Fake Context. No implementation")
    }

    override fun checkCallingOrSelfUriPermission(uri: Uri?, modeFlags: Int): Int {
        TODO("Fake Context. No implementation")
    }

    override fun enforceUriPermission(uri: Uri?, pid: Int, uid: Int, modeFlags: Int, message: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun enforceUriPermission(uri: Uri?, readPermission: String?, writePermission: String?, pid: Int, uid: Int, modeFlags: Int, message: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun enforceCallingUriPermission(uri: Uri?, modeFlags: Int, message: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun enforceCallingOrSelfUriPermission(uri: Uri?, modeFlags: Int, message: String?) {
        TODO("Fake Context. No implementation")
    }

    override fun createPackageContext(packageName: String?, flags: Int): Context {
        TODO("Fake Context. No implementation")
    }

    override fun createContextForSplit(splitName: String?): Context {
        TODO("Fake Context. No implementation")
    }

    override fun createConfigurationContext(overrideConfiguration: Configuration): Context {
        TODO("Fake Context. No implementation")
    }

    override fun createDisplayContext(display: Display): Context {
        TODO("Fake Context. No implementation")
    }

    override fun createDeviceProtectedStorageContext(): Context {
        TODO("Fake Context. No implementation")
    }

    override fun isDeviceProtectedStorage(): Boolean {
        TODO("Fake Context. No implementation")
    }
}