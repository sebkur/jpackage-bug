# JPackage bug

A minimalist project showcasing a bug found when packaging distributable
apps using `jpackage` on Linux.

This project contains a single source file `App.java` and a script `package.sh`
for compiling it and packaging it into a distributable app using `jpackage`.

To test this with a JDK of your choice, edit `package.sh` and specify the JDK
to test in the `JAVA` variable.

First, package:

    ./package.sh

Usage is like this:

    ./App/bin/App [command] <[args...]>

For example, you can type this:

    ./App/bin/App ls -og

and the output will be something like this:

    [stdout] total 28
    [stdout] drwxrwxr-x 4 4096 Jun 23 15:52 App
    [stdout] -rw-rw-r-- 1 1043 Jun 23 15:45 App.java
    [stdout] drwxrwxr-x 2 4096 Jun 23 15:52 dist
    [stdout] drwxrwxr-x 3 4096 Jun 23 15:36 out
    [stdout] -rwxrw-r-- 1  356 Jun 23 15:52 package.sh
    [stdout] -rw-rw-r-- 1  536 Jun 23 15:56 README.md

When you run it without any arguments, you get this output:

    Please specify a command to run and optional arguments

## Actual bug

Now, the actual bug is this. Try running the app recursively, i.e.
launch it and instruct it to launch itself a second time:

    ./App/bin/App ./App/bin/App

Expected output:

    [stdout] Please specify a command to run and optional arguments

This is what happens:

    [stdout] Usage: java [options] <mainclass> [args...]
    [stdout]            (to execute a class)
    [stdout]    or  java [options] -jar <jarfile> [args...]
    [stdout]            (to execute a jar file)
    [stdout]    or  java [options] -m <module>[/<mainclass>] [args...]
    [stdout]        java [options] --module <module>[/<mainclass>] [args...]
    [stdout]            (to execute the main class in a module)
    [stdout]    or  java [options] <sourcefile> [args]
    [stdout]            (to execute a single source-file program)
    [stdout] 
    [stdout]  Arguments following the main class, source file, -jar <jarfile>,
    [stdout]  -m or --module <module>/<mainclass> are passed as the arguments to
    [stdout]  main class.
    [stdout] 
    [stdout]  where options include:
    [stdout] 
    [stdout]     -cp <class search path of directories and zip/jar files>
    [stdout]     -classpath <class search path of directories and zip/jar files>
    [stdout]     --class-path <class search path of directories and zip/jar files>
    [stdout]                   A : separated list of directories, JAR archives,
    [stdout]                   and ZIP archives to search for class files.
    [stdout]     -p <module path>
    [stdout]     --module-path <module path>...
    [stdout]                   A : separated list of directories, each directory
    [stdout]                   is a directory of modules.
    [stdout]     --upgrade-module-path <module path>...
    [stdout]                   A : separated list of directories, each directory
    [stdout]                   is a directory of modules that replace upgradeable
    [stdout]                   modules in the runtime image
    [stdout]     --add-modules <module name>[,<module name>...]
    [stdout]                   root modules to resolve in addition to the initial module.
    [stdout]                   <module name> can also be ALL-DEFAULT, ALL-SYSTEM,
    [stdout]                   ALL-MODULE-PATH.
    [stdout]     --enable-native-access <module name>[,<module name>...]
    [stdout]                   modules that are permitted to perform restricted native operations.
    [stdout]                   <module name> can also be ALL-UNNAMED.
    [stdout]     --list-modules
    [stdout]                   list observable modules and exit
    [stdout]     -d <module name>
    [stdout]     --describe-module <module name>
    [stdout]                   describe a module and exit
    [stdout]     --dry-run     create VM and load main class but do not execute main method.
    [stdout]                   The --dry-run option may be useful for validating the
    [stdout]                   command-line options such as the module system configuration.
    [stdout]     --validate-modules
    [stdout]                   validate all modules and exit
    [stdout]                   The --validate-modules option may be useful for finding
    [stdout]                   conflicts and other errors with modules on the module path.
    [stdout]     -D<name>=<value>
    [stdout]                   set a system property
    [stdout]     -verbose:[class|module|gc|jni]
    [stdout]                   enable verbose output for the given subsystem
    [stdout]     -version      print product version to the error stream and exit
    [stdout]     --version     print product version to the output stream and exit
    [stdout]     -showversion  print product version to the error stream and continue
    [stdout]     --show-version
    [stdout]                   print product version to the output stream and continue
    [stdout]     --show-module-resolution
    [stdout]                   show module resolution output during startup
    [stdout]     -? -h -help
    [stdout]                   print this help message to the error stream
    [stdout]     --help        print this help message to the output stream
    [stdout]     -X            print help on extra options to the error stream
    [stdout]     --help-extra  print help on extra options to the output stream
    [stdout]     -ea[:<packagename>...|:<classname>]
    [stdout]     -enableassertions[:<packagename>...|:<classname>]
    [stdout]                   enable assertions with specified granularity
    [stdout]     -da[:<packagename>...|:<classname>]
    [stdout]     -disableassertions[:<packagename>...|:<classname>]
    [stdout]                   disable assertions with specified granularity
    [stdout]     -esa | -enablesystemassertions
    [stdout]                   enable system assertions
    [stdout]     -dsa | -disablesystemassertions
    [stdout]                   disable system assertions
    [stdout]     -agentlib:<libname>[=<options>]
    [stdout]                   load native agent library <libname>, e.g. -agentlib:jdwp
    [stdout]                   see also -agentlib:jdwp=help
    [stdout]     -agentpath:<pathname>[=<options>]
    [stdout]                   load native agent library by full pathname
    [stdout]     -javaagent:<jarpath>[=<options>]
    [stdout]                   load Java programming language agent, see java.lang.instrument
    [stdout]     -splash:<imagepath>
    [stdout]                   show splash screen with specified image
    [stdout]                   HiDPI scaled images are automatically supported and used
    [stdout]                   if available. The unscaled image filename, e.g. image.ext,
    [stdout]                   should always be passed as the argument to the -splash option.
    [stdout]                   The most appropriate scaled image provided will be picked up
    [stdout]                   automatically.
    [stdout]                   See the SplashScreen API documentation for more information
    [stdout]     @argument files
    [stdout]                   one or more argument files containing options
    [stdout]     -disable-@files
    [stdout]                   prevent further argument file expansion
    [stdout]     --enable-preview
    [stdout]                   allow classes to depend on preview features of this release
    [stdout] To specify an argument for a long option, you can use --<name>=<value> or
    [stdout] --<name> <value>.
    [stdout] 

Also, this should work:

    ./App/bin/App ./App/bin/App ls

Expected output:

    [stdout] [stdout] App
    [stdout] [stdout] App.java
    [stdout] [stdout] dist
    [stdout] [stdout] jpackagebug.iml
    [stdout] [stdout] out
    [stdout] [stdout] output
    [stdout] [stdout] package.sh
    [stdout] [stdout] README.md

Actual output:

    [stdout] Error: Could not find or load main class ls
    [stdout] Caused by: java.lang.ClassNotFoundException: ls

You can convince yourself that it works when launching the app in a more classic
way:

    java -cp out/ App java -cp out/ App ls

What also works is this:

    java -cp out/ App ./App/bin/App ls

And this:

    ./App/bin/App java -cp out/ App ls
