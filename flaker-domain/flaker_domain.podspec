Pod::Spec.new do |spec|
    spec.name                     = 'flaker_domain'
    spec.version                  = '0.1.0'
    spec.homepage                 = 'https://github.com/RotBolt/Flaker'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Multiplatform Kotlin flaker library'
    spec.vendored_frameworks      = 'build/cocoapods/framework/FlakerDomainModule.framework'
    spec.libraries                = 'c++'
                
                
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':flaker-domain',
        'PRODUCT_MODULE_NAME' => 'FlakerDomainModule',
    }
                
    spec.script_phases = [
        {
            :name => 'Build flaker_domain',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end