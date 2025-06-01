# Expense Tracker

## Version Information
- **Current Version:** 1.0.0
- **Last Updated:** 2024-03-27

## Overview
This project is an Android application designed to help users track their expenses. It allows users to import transaction data from files, review and categorize transactions, and view detailed reports.

## Features
- **File Import:** Import transaction data from CSV or other file formats.
- **Transaction Review:** Review and categorize imported transactions.
- **Expense Reports:** View detailed reports of your expenses.

## Recent Changes
### Version 1.0.0 (2024-03-27)
- Fixed a crash in the Review window by ensuring that the layout used in the ViewPager2 has its root view's width and height set to `match_parent`.
- Improved error handling and user feedback during file processing.
- Enhanced database versioning and migration to handle schema changes gracefully.
- Added proper version control and documentation.

## Version Control
This project uses Git for version control. Here's how to work with it:

### Getting Started
1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd Expense\ Tracker
   ```

2. **Create a new branch for your changes:**
   ```bash
   git checkout -b feature/your-feature-name
   ```

### Making Changes
1. **Update the version number:**
   - For bug fixes: Increment the patch version (1.0.0 -> 1.0.1)
   - For new features: Increment the minor version (1.0.0 -> 1.1.0)
   - For breaking changes: Increment the major version (1.0.0 -> 2.0.0)

2. **Commit your changes:**
   ```bash
   git add .
   git commit -m "Description of changes"
   ```

3. **Push your changes:**
   ```bash
   git push origin feature/your-feature-name
   ```

### Version History
- **1.0.0 (2024-03-27)**
  - Initial release
  - Basic expense tracking functionality
  - File import and review features
  - Database integration

## How to Build
1. **Open the project in Android Studio:**
   - Launch Android Studio and select "Open an existing project."
   - Navigate to the cloned directory and open it.

2. **Build the project:**
   - In Android Studio, click on "Build" > "Make Project" or use the shortcut `Ctrl + F9` (Windows/Linux) or `Cmd + F9` (Mac).
   - Alternatively, you can build from the command line:
     ```bash
     ./gradlew build
     ```

## How to Test New Changes
1. **Run the app on an emulator or physical device:**
   - In Android Studio, click on "Run" > "Run 'app'" or use the shortcut `Shift + F10` (Windows/Linux) or `Control + R` (Mac).
   - Ensure that you have an emulator set up or a physical device connected.

2. **Test the new features:**
   - Import a transaction file and verify that the Review window works without crashing.
   - Check that the layout in the ViewPager2 fills the entire screen.
   - Verify that error handling and user feedback are functioning as expected.

3. **Debugging:**
   - If you encounter any issues, check the logcat output in Android Studio for error messages and stack traces.
   - Use the `adb logcat` command to capture detailed logs if needed.

## Contributing
If you would like to contribute to this project, please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them with a descriptive message.
4. Push your branch to your fork and create a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for more details. 