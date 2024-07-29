#!/bin/bash

# Navigate to the frontend directory and package the frontend
echo "Packaging frontend"
cd frontend || { echo "Failed to navigate to frontend directory"; exit 1; }
flutter config --enable-web
flutter build web || { echo "Frontend build failed"; exit 1; }
echo "Frontend Packaging successfully"

cd .. # Navigate back to the project root directory

# Navigate to the backend directory and package the backend
echo "Packaging backend"
cd backend || { echo "Failed to navigate to backend directory"; exit 1; }
./gradlew clean build -Dspring.profiles.active=deploy || { echo "Backend build failed"; exit 1; }
echo "Backend Packaging successfully"

cd .. # Navigate back to the project root directory

echo "Done"