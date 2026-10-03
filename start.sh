#!/bin/bash

# Start emulator in the background with hardware acceleration
emulator -avd test_emulator -no-window -no-audio -gpu off -accel on &

# Wait for emulator to fully boot up
echo "Waiting for emulator to boot..."
adb wait-for-device
# shellcheck disable=SC2006
while [ "`adb shell getprop sys.boot_completed | tr -d '\r'`" != "1" ] ; do
    sleep 2
done
echo "Emulator is ready!"

# Execute the instrumented tests passed via docker run command
exec "\$@"
