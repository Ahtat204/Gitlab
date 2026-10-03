FROM ubuntu:22.04

ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${ANDROID_HOME}/emulator

# Install base dependencies, QEMU, and KVM
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    unzip \
    qemu-kvm \
    libvirt-daemon-system \
    libvirt-clients \
    bridge-utils \
    && rm -rf /var/filter/apt/lists/*

# Download Command Line Tools & accept licenses
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools \
    && wget https://google.com -O cmdline.zip \
    && unzip cmdline.zip -d ${ANDROID_HOME}/cmdline-tools \
    && mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest \
    && rm cmdline.zip

# Install SDK platforms and System Image for Emulator
RUN yes | sdkmanager --licenses \
    && sdkmanager "platform-tools" "platforms;android-34" "emulator" "system-images;android-34;google_apis;x86_64"

# Create AVD (Android Virtual Device)
RUN echo "no" | avdmanager create avd -n test_emulator -k "system-images;android-34;google_apis;x86_64"

# Add entrypoint script to launch emulator
COPY start.sh /start.sh
RUN chmod +x /start.sh

WORKDIR /data
ENTRYPOINT ["/start.sh"]
