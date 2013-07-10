Version 3.0 Update
==================

The Version 3.0 Update includes support for UIAutomation. The MTD tool now asks the device, if it is running Jelly Bean 4.1 (API 16) or later, for the UIAutomation dump and will use that across devices to do more intelligent picking. If the view to be clicked is not in the active view area the MTD tool will scroll to find it. This gives a greater success rate in clicking the correct view across devices. 

***
***

Introduction
=========

The Manual Test Demultiplexer is a desktop app (Mac and Windows) that provides
an interface for driving manual testing on multiple physical devices. The only
limiting factor is the limited number of physical USB connections on a
desktop/laptop. Use of a USB hub is recommend.

MTD communicates all input to each device connected to your system. Instead of
running through manual tests one device at a time, MTD allows you to do manual
testing on multiple devices at the same time. All clicks in the MTD device
window are sent to all devices. In other words, clicking on your app's buy
button in MTD clicks on the buy button on each device. Each click is sent as a
touch event at that screen location (scaling between different screen sizes is
considered). Drag events, keyboard input as well as hardware buttons are
supported. There are also buttons for routine operations such as
install/uninstall apks, start activities, reboot devices, execute raw adb
commands, wake & unlock devices, toggle airplane mode, capture screen and save
out bugreport (adb bugreport). MTD also supports switching between each of the
connected devices screens. MTD does not act as an emulator. It instead polls
one of the connected devices for its screen and shows that in the GUI.
Therefore, all hardware capabilities are available during testing.

Every interaction is recorded and can be played back in the same sequence.
If you encounter a crash, you have the entire history of your actions recorded
to replay as repro steps. These can be saved and reloaded for future use.

There are also tools for performing login on each device. There is a login
file that is parsed to apply a unique login & password for each device
connected. Account logins are not a stumbling block with multiple devices
attached.

Please direct comments and questions to:  
Benjamin Yarger  
<byarger@ebay.com>

***
***

Building the Application
==================

The ManualTestDemultiplexer (MTD) requires the Android SDK to be installed in
order to compile and run. MTD has build dependencies in the SDK and uses adb
to communicate with attached devices.

Quick Start
-----------------

Run the following commands in terminal to build and run the MTD tool. Presumes you are starting from the mtdtool directory. Make sure your ANDROID\_HOME and ADB\_HOME environment variables are set before proceeding (see Full Explanation for more info).
<pre><code>cd ManualTestDemultiplexer/
ant
java -jar MTD.jar
</code></pre>

Environment Variables
------------
Hopefully your SDK configuration set the ANDROID\_HOME environment variable to the root of your android-sdk directory. If not, add it and the build scripts should resolve the dependencies
correctly.

MTD execution requires that the ADB\_HOME environment variable is set to the
path for adb.

For example:
export ADB\_HOME=/Developer/android-sdk-macosx/platform-tools/adb

Full Explanation
-----------------------

MTD requires the following libraries to compile:

* /android-sdk/tools/lib/ddmlib.jar
* /android-sdk/tools/lib/chimpchat.jar
* /android-sdk/tools/lib/sdklib.jar
* /android-sdk/tools/lib/guava-13.0.1.jar
* /android-sdk/tools/lib/uiautomation.jar

All of these jars come from the Android SDK tools/lib folder.

There are two project dependencies (these are found in the mtdtool directory): 

* DeviceUnlock
* ToggleAirplaneMode. 

The functionality to wake and unlock devices and toggle airplane mode is supported
by these additonal projects. You will need the apks generated by these two
projects in MTD. Fortunately, ant will take care of this for you (see Quick Start above).

Here are a few of the most used ant targets available from the build script in the ManualTestDemultiplexer directory.

* ant             - Default will build all projects and create MTD.jar
* ant clean       - Clean the MTD project
* ant cleanall    - Clean MTD, DeviceUnlock and ToggleAirplaneMode
* ant build       - Build all three projects

***
***

FAQ:
=====
Q: How do I determine the package name and start activity for a given apk?  
A: Use the following command line arguments to determine the start activity string to use:
<code><pre>
aapt dump badging \[path to apk\] | grep -E "^launchable-activity|^package" | cut -d"'" -f2 | sed 'N;s/\n/\//'
</pre></code>

Note that this assumes you have aapt on your path.

***

Q: What ports do I need to forward to use MTD for remote control of a device and remote debugging?  
A: Using SSH it is possible to tunnel MTD connections. You need to forward the following ports: 5037, 5554, 5555 & 12345. The typical SSH command looks like:
<code><pre>
ssh -L 127.0.0.1:5037:127.0.0.1:5037 -L 127.0.0.1:5554:127.0.0.1:5554 -L 127.0.0.1:5555:127.0.0.1:5555 -L 127.0.0.1:12345:127.0.0.1:12345 < username >@< ip address of SSH server >
</pre></code>

***

Q: I am having problems seeing my remote device over SSH.  
A: Before you establish the SSH connection with the server, first kill the local adb connection 'adb kill-server'. Next, confirm that the device is visible on the SSH server 'adb devices'. Then connect with the SSH server. Open another terminal window, DO NOT use the same terminal window as the SSH session, and start the adb connection on the local machine 'adb devices'. That should show you the serial number of the device attached to the SSH server. Now launch MTD and you will have full control of that remote device, including debugging from Eclipse.

***

Q: I'm not hitting the same button on all of my attached devices.  
A: It is a good idea to group your test devices according to screen size. If your layouts adjust the number or position of elements on screen based on screen size you will find that clicking of UI elements is inconsistent. This is due to the change of relative position caused by different screen areas. Even with the scaling calculations there isn't a good way to make sure the correct GUI element is clicked across devices with significantly different screen sizes.

***

Q: Why isn't device wake and unlock working?  
A: In order for device wake and unlock to work you have to change your security settings to use a pin instead of swipe to unlock. Set your pin to 1234 and the device will unlock.

