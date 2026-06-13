# 📱 VerifiNews (Project 2)

**SDG Theme:** SDG 16: Peace, Justice and Strong Institutions

## 📖 Project Description
VerifiNews is a fully connected, persistent, and hardware-aware mobile application designed to combat the spread of fake news and misinformation. Upgraded from the Project 1 UI prototype, this Project 2 build integrates live data from the internet, cloud database syncing, local offline storage, and hardware sensor integration. It empowers users to fetch live verified news, save facts offline, and actively report suspicious viral claims using their device's camera.

## ⚠️ Problem Statement
Citizens lack a reliable platform to not only verify viral claims but also actively report suspicious news with photo evidence directly to a community moderation queue.

## ✨ Key Features (Project 2 Upgrades)
* **Internet Data Integration (Web API):** Connects to a live REST API (NewsAPI) using Retrofit to fetch and dynamically display real-time news articles.
* **Cloud Integration (Firebase Firestore):** Utilizes a NoSQL cloud database to store community-reported fake news, enabling a real-time admin-verification workflow.
* **Local Persistence (Room Database):** Allows users to save verified news articles permanently on their local device storage for offline reading.
* **Hardware Sensor Integration:** Uses the device's Camera sensor (`ActivityResultContracts.TakePicturePreview`) to capture and upload photographic proof when reporting misinformation.
* **Expanded Navigation:** A comprehensive 7-screen flow including Home, Profile, Report, Report List, AI Fact-Checker, Bookmarks, and News Details.
* **Hybrid Data Management:** Combines live API data and Firebase cloud data dynamically in the ViewModel.

## 🛠 Technologies Used
* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose, Material Design 3
* **Architecture:** MVVM (Model-View-ViewModel), Navigation Compose
* **Local Database:** Room Database (SQLite), Kotlin Coroutines & Flow
* **Cloud Database:** Firebase Firestore
* **Network & API:** Retrofit, GsonConverterFactory, Gemini AI SDK
* **Hardware & Media:** Camera Intent, Coil (Image Loading), Base64 Encoding

## 🚀 Setup Instructions (Crucial for Running the App)
For security reasons, API keys and cloud configuration files are **not** uploaded to this repository. To run this project perfectly on your local machine, please follow these steps:

1. **Clone** the repository to your local machine.
2. **Open** the project in Android Studio.
3. **Add Firebase Configuration:**
   * Obtain the `google-services.json` file for this Firebase project.
   * Place the `google-services.json` file directly inside the `app/` directory of the project.
4. **Configure Hidden API Keys:**
   * Open the `local.properties` file located in the root directory of the project.
   * Add your API keys at the very bottom of the file exactly like this (without quote marks):
     ```properties
     NEWS_API_KEY=insert_your_newsapi_key_here
     GEMINI_API_KEY=insert_your_gemini_api_key_here
     ```
   * *(Note: Get your NewsAPI key from newsapi.org and Gemini key from Google AI Studio).*
5. **Sync and Generate BuildConfig:** * Click **"Sync Project with Gradle Files"**. 
   * Then, go to the top menu and select **Build > Rebuild Project** (or Clean Project then Run). This forces Android Studio to generate the `BuildConfig` class that injects the API keys securely into the code.
6. **Build and Run:** Run the application on a physical Android device (highly recommended for testing the camera sensor) or an Android Virtual Device (Emulator).

## 🧠 Learning Outcomes
This project pushed my technical boundaries by integrating Firebase Firestore for cloud syncing and Retrofit for fetching live data from external APIs. I learned how to handle device hardware (Camera sensor) to process image data via Base64, manage database migrations in Room, and successfully merge hybrid data streams (local, cloud, and live API) into a unified, reactive UI using Jetpack Compose while prioritizing API security.

---

## 👨‍💻 Developer Information
* **Developer:** Mohamad Khairi Hafizi bin Mohd Nazri
* **Matric Number:** A214592
* **Course:** TK2323 / TM2213 Mobile Programming / Mobile Application Programming
* **Instructor:** Mr. Nelson Sana
