# FLTR: Foreign Language Text Reader

## What Problem Does This Solve?
FLTR (Foreign Language Text Reader) is a specialized tool designed to help language learners read texts in foreign languages. It addresses the challenge of reading comprehension by providing an interactive environment where users can look up unknown words, save vocabulary, and track their learning progress. This fork maintains and improves upon the original abandoned SourceForge project.

## Who Is This For?
- Language learners at intermediate to advanced levels
- Language teachers creating reading materials for students
- Self-study language enthusiasts
- Anyone who wants to improve reading comprehension in a foreign language

## Current Implementation Status
- ✅ Text import and management
- ✅ Word lookup and vocabulary tracking
- ✅ Term status tracking (known/unknown/learning)
- ✅ Language definition management
- ✅ Fuzzy search capabilities
- ✅ User preferences and settings
- 🚧 Modern UI improvements
- 🚧 Additional language support
- 📋 Mobile/tablet support
- 📋 Cloud synchronization

## Setup Instructions

### Prerequisites
- Java Runtime Environment (JRE) 8 or higher
- Basic understanding of Java applications

### Installation
1. Clone the repository:
   ```
   git clone https://github.com/nczempin/fltr.git
   cd fltr
   ```

2. Compile the source code:
   ```
   javac -d bin -sourcepath src -cp lib/miglayout-4.0.jar src/fltrpackage/Application.java
   ```

3. Copy resources to the bin directory:
   ```
   cp src/fltrpackage/icon128.png bin/fltrpackage/
   ```

4. Create a runnable JAR file (optional):
   ```
   jar cvfm fltr.jar manifest.txt -C bin .
   ```

### Running the Application
1. Run from compiled classes:
   - On Linux/Mac:
     ```
     java -cp "bin:lib/miglayout-4.0.jar" fltrpackage.Application
     ```
   - On Windows:
     ```
     java -cp "bin;lib/miglayout-4.0.jar" fltrpackage.Application
     ```

2. Or run from the JAR file (if created):
   ```
   java -jar fltr.jar
   ```

## Project Scope

### What This IS
- A tool for reading and studying foreign language texts
- A vocabulary management system for language learners
- A fork that maintains and improves upon the abandoned original project
- A desktop Java application for personal language learning

### What This IS NOT
- Not a translation tool or dictionary (though it helps with vocabulary)
- Not a language course or complete learning system
- Not optimized for mobile devices or tablets
- Not a commercial product with support services

## Repository Structure
- `src/fltrpackage/` - Main application source code
  - `Application.java` - Main entry point
  - `StartFrame.java` - Initial UI frame
  - `TextFrame.java` - Text reading interface
  - `TermFrame.java` - Vocabulary management
  - `Language.java` - Language definition handling
  - `Text.java` - Text content management
  - `Term.java` - Vocabulary term representation
  - Various UI components and utility classes

## Basic Usage
1. Start the application
2. Create or select a language definition
3. Import or create a text in your target language
4. Click on words you don't know to mark them and add translations
5. Track your progress as you learn new vocabulary

## Original Project
This is a fork of the original FLTR project from SourceForge (https://sourceforge.net/projects/fltr/), which appears to be abandoned. This repository uses Git for version control rather than distributing source code in ZIP files.

## Development Status
This is a maintained fork of an abandoned project. Contributions and feedback are welcome.
