# Rakta-Vahini 🩸
### *Intelligent Blood Donation Management System*

**Rakta-Vahini** is a professional Android prototype designed to bridge the gap between blood donors and seekers. It automates the complex medical eligibility tracking process and provides a real-time discovery platform to save lives during emergencies.

---

## 🚀 Key Features

- **Personalized Dashboard:** Real-time statistics showing total donors, total donations, and community impact.
- **Smart Donor Search:** Filter donors by blood group with an automated medical "cooldown" check.
- **Automated Eligibility Engine:** Programmatically enforces the **90-day recovery rule** using Java Time APIs.
- **Secure Profile Management:** Users can register as donors, update health details, and toggle their "Ready to Donate" status.
- **Donation Logging:** A structured workflow to record donations, which automatically resets the donor's eligibility timer.
- **Instant Notifications:** Automated "Thank You" alerts and system feedback post-donation.

---

## 🛠️ Technical Stack

- **Language:** Kotlin
- **UI Framework:** XML with **ViewBinding** for type-safe interactions.
- **Design:** Material 3 (M3) Design components.
- **Database:** **Room Persistence Library** (SQLite abstraction) for local data management.
- **Concurrency:** **Kotlin Coroutines** for non-blocking database operations.
- **Architecture:** Layered architecture (UI -> Logic/Utility -> Data).

---

## 📂 Project Structure

- `com.example.raktvahini.data`: Contains Room Entities, DAOs, and Database configuration.
- `com.example.raktvahini.ui`: Custom adapters (RecyclerView) for displaying donor lists.
- `com.example.raktvahini.utils`: The "Brain" of the app, including the `DonorEligibilityFilter`.
- `com.example.raktvahini.activities`: All screen logic (Home, Search, Profile, Edit Profile).

---

## 📦 How to Run

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/RaktVahini.git
    ```
2.  **Open in Android Studio:**
    - File > Open > Select `RaktVahini` folder.
3.  **Sync Gradle:**
    - Wait for the project to sync dependencies.
4.  **Run:**
    - Connect an Android device or use an Emulator (API 26+).
    - Click the **Run** button in Android Studio.

---

## 📈 Future Enhancements

- **Google Maps Integration:** Real-time distance-based donor discovery.
- **Hospital API:** Syncing with live hospital blood bank inventories.
- **Biometric Security:** Fingerprint/FaceID login for medical records.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Created for Academic Prototype Demonstration.*
