# FLTR: Foreign Language Text Reader

> **⚠️ IMPORTANT NOTICE: This is an UNOFFICIAL fork**
> 
> This repository was created in 2017 when the FLTR project appeared to be abandoned. However, the original developers have since resumed maintenance of FLTR.
> 
> **For the official, up-to-date version of FLTR, please visit:**
> - **Official Website:** https://foreign-language-text-reader.sourceforge.io
> - **Official SourceForge Project:** https://sourceforge.net/projects/foreign-language-text-reader/
> 
> This fork is kept as a historical archive and is NOT actively maintained. Users are strongly encouraged to use the official version instead.

## About FLTR

FLTR (Foreign Language Text Reader) helps you do both extensive and intensive reading as part of your foreign language acquisition in an easy and pleasant way.

While reading, you look up unknown words in web dictionaries (online) or locally installed dictionary applications, save vocabulary terms with translations, and track your learning progress. Each term has a learning status (1/"Unknown" to 5/"Known", plus "Ignored" and "Well Known") with associated colors.

For more details about features and usage, please visit the official documentation:
- **Official Website:** https://foreign-language-text-reader.sourceforge.io

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


## Fork History
This repository was created in July 2017 when the original FLTR project appeared to be abandoned on SourceForge. The code was imported to preserve it and make it available via Git.

The original developers resumed activity in 2020 and released multiple versions (0.8.10, 1.0.0, 1.1.0, 1.2.0, 1.3.0) before releasing FLTR version 1.4.0 in June 2021. They continue to maintain both FLTR and their related project LWT (Learning with Texts).

### Bug Fixes in This Fork
This fork contains two bug fixes from 2020 that have NOT been incorporated into the official version 1.4.0:
1. **NPE fix for missing icons** - Prevents crashes when icon files cannot be loaded
2. **Java version check fix** - Fixes version comparison for Java 9+ (though the official version removed this check entirely)

### Other GitHub Forks
- **magnus-ISU/foreign-language-text-reader** - Another GitHub fork with prebuilt JAR files and Linux installation scripts: https://github.com/magnus-ISU/foreign-language-text-reader

## Development Status
**This fork is NOT actively maintained.** It exists as a historical archive of the 2017 codebase with minor bug fixes. 

For active development and support, please use the official FLTR at:
- https://foreign-language-text-reader.sourceforge.io
