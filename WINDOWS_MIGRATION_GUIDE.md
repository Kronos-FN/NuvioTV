# WINDOWS MIGRATION GUIDE

This guide provides detailed instructions on converting the NuvioTV application to a Windows desktop application.

## Prerequisites
- Ensure you have a Windows environment set up with the necessary tools installed:
  - Visual Studio (2022 or later)
  - .NET SDK (installed according to your application's requirement)
  - A Git client for version control

## Step 1: Clone the Repository
Clone the latest version of the NuvioTV repository to your local machine using the following command:

```bash
git clone https://github.com/Kronos-FN/NuvioTV.git
```

## Step 2: Open the Project in Visual Studio
- Open Visual Studio.
- Select `Open a project or solution`.
- Navigate to the cloned NuvioTV directory and select the solution file (`.sln`).

## Step 3: Update Project Properties
- In Solution Explorer, right-click on the project and select `Properties`.
- Under the `Application` tab, ensure the Output type is set to `Windows Application`.
- Adjust the Target Framework to the appropriate version compatible with Windows.

## Step 4: Modify Code for Windows Compatibility
- Address any platform-specific code, replacing or modifying sections that are not compatible with Windows.
- Utilize Windows API integrations where necessary to enhance application performance on Windows.

## Step 5: Add Windows-Specific Resources
- Include any icons, splash screens, or Windows-specific resources in the project.
- Update the project to reference these resources correctly.

## Step 6: Testing the Application
- Build the application by selecting `Build > Build Solution`.
- Run the application to ensure it operates as intended in the Windows environment. Debug any issues that arise.

## Step 7: Create an Installer (Optional)
- If you wish to distribute your application, consider creating an installer using tools such as Inno Setup or WiX.
- Follow the installer creation tool's documentation to package your application.

## Step 8: Commit Changes
- Once you have successfully converted and tested the application, commit your changes to the repository:

```bash
git add .
git commit -m "Converted NuvioTV to Windows desktop application"
git push origin main
```

## Conclusion
This guide has walked you through converting the NuvioTV application into a Windows desktop application. Ensure you thoroughly test your application and address any further requirements for deployment.