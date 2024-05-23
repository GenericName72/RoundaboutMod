# MultiLoader Template

This project provides a Kotlin-DSL Gradle project template with a version catalogue and convention plugin that can compile mods for both Forge and Fabric using a common sourceset. This project does not require any third party libraries or dependencies and includes [MixinExtras](https://github.com/LlamaLad7/MixinExtras).

## Credits

Credits for this template go to Tslat/Witixin's [Geckolib](https://github.com/bernie-g/geckolib) setup and Jared [MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template/tree/1.20.4), this template is a modified version of theirs to allow for Kotlin-DSL and version catalogues. Even this README is very similar to theirs.

## Getting Started

### IntelliJ IDEA
This guide will show how to import the MultiLoader Template into IntelliJ IDEA. The setup process is roughly equivalent to setting up Forge and Fabric independently and should be very familiar to anyone who has worked with their MDKs.

1. Clone or download this repository to your computer.
2. Configure the project by editing the `group`, `mod_name`, `mod_id`, and `author` properties in the `gradle.properties` file. You will also need to change the `rootProject.name` property in `settings.gradle`, this should match the folder name of your project, or else IDEA may complain.
3. Rename all files containing `examplemod` in their name to match the `mod_id` you set in `gradle.properties`. This includes `examplemod.mixin.json` and `examplemod.accesswidener` in each subproject.
4. Open the template's root folder as a new project in IDEA. This is the folder that contains this README file and the gradlew executable.
5. If your default JVM/JDK is not Java 17 you will encounter an error when opening the project. This error is fixed by going to `File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM`and changing the value to a valid Java 17 JVM. You will also need to set the Project SDK to Java 17. This can be done by going to `File > Project Structure > Project SDK`. Once both have been set open the Gradle tab in IDEA and click the refresh button to reload the project.
6. Open the Gradle tab in IDEA if it has not already been opened. Navigate to `Your Project > Common > Tasks > vanilla gradle > decompile`. Run this task to decompile Minecraft.
7. Open the Gradle tab in IDEA if it has not already been opened. Navigate to `Your Project > Forge > Tasks > forgegradle runs > genIntellijRuns`. Run this task to set up run configurations for Forge.
8. Open your Run/Debug Configurations. Under the Application category there should now be options to run Forge and Fabric projects. Select one of the client options and try to run it.
9. Assuming you were able to run the game in step 8 your workspace should now be set up.

## Development Guide
When using this template the majority of your mod is developed in the Common project. The Common project is compiled against the vanilla game and is used to hold code that is shared between the different loader-specific versions of your mod. The Common project has no knowledge or access to ModLoader specific code, apis, or concepts. Code that requires something from a specific loader must be done through the project that is specific to that loader, such as the Forge or Fabric project.

Loader specific projects such as the Forge and Fabric project are used to load the Common project into the game. These projects also define code that is specific to that loader. Loader specific projects can access all of the code in the Common project. It is important to remember that the Common project can not access code from loader specific projects.

## Adding Dependencies

Unlike other Minecraft Modding MDKs or Templates, this one utilises a version catalogue. To properly add a dependency or change the version of your mod, it must be added to `gradle/libs.versions.toml` in addition to your build scripts. If your dependency is another mod, you should also add it to each platform's mod file and create a property for it in `multiloader-convention.gradle.kts` so it can be expanded.

## Removing Platforms and Loaders
While the MultiLoader Template includes support for many platforms and loaders you can easily remove support for the ones you don't need. This can be done by deleting the subproject folder and then removing it from the `settings.gradle` file. For example if you wanted to remove support for Forge you would follow the following steps. 

1. Delete the subproject folder. For example, delete `MultiLoader-Template/forge`.
2. Remove the project from `settings.gradle`. For example, remove `include("forge")`. 
