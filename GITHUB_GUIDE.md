# Guide: Pushing Rakta-Vahini to GitHub

Follow these steps to upload your project to GitHub for the first time.

### 1. Create a Repository on GitHub
1.  Log in to [GitHub](https://github.com/).
2.  Click the **+** icon in the top right and select **New repository**.
3.  Name it `RaktVahini`.
4.  Keep it **Public** (for your demo) and **do not** initialize with a README or .gitignore (as I have already created them for you).
5.  Click **Create repository**.

### 2. Initialize Git Locally
Open the terminal in Android Studio (at the bottom) and run:

```bash
# Initialize the git repository
git init

# Add all files to the staging area
git add .

# Create your first commit
git commit -m "Initial commit: Rakta-Vahini Complete Prototype"
```

### 3. Connect and Push
Copy the URL of your GitHub repository (e.g., `https://github.com/yourusername/RaktVahini.git`) and run:

```bash
# Rename branch to main
git branch -M main

# Add the remote destination (Replace URL with your actual repo URL)
git remote add origin https://github.com/yourusername/RaktVahini.git

# Push the code to GitHub
git push -u origin main
```

### 4. Verification
Refresh your GitHub page. You should now see all your folders (`app/`, `gradle/`) and the `README.md` beautifully rendered.

---
### ⚠️ Important Note
**Do not** delete the `.gitignore` file. It ensures that large temporary files and your personal `local.properties` (SDK paths) are not uploaded to GitHub.
