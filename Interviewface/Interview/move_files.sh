#!/bin/bash
# Move all files from src/ to app/src/
mkdir -p app/src/main/java/com/example/interviewface
mkdir -p app/src/main/res/layout
mkdir -p app/src/main/res/values
mkdir -p app/src/main/res/drawable
mkdir -p app/src/main/res/menu
mkdir -p app/src/main/res/color

# Move Java/Kotlin files
mv src/main/java/com/example/interviewface/* app/src/main/java/com/example/interviewface/

# Move resource files
mv src/main/res/layout/* app/src/main/res/layout/
mv src/main/res/values/* app/src/main/res/values/
mv src/main/res/drawable/* app/src/main/res/drawable/
mv src/main/res/menu/* app/src/main/res/menu/
mv src/main/res/color/* app/src/main/res/color/

# Move AndroidManifest.xml
mv src/main/AndroidManifest.xml app/src/main/AndroidManifest.xml

# Clean up empty directories
rm -rf src/
