# Advanced Inventory

This project contains a small Forge mod for Minecraft 1.20.1.
Its only purpose now is to adjust the maximum stack size for every item.
The stack size is controlled through the `advancedinventory-server.toml` configuration file
and defaults to `50000` items per stack. Setting the value to `-1` restores the
vanilla limit.

## Building

This project is built using Gradle. Typical build steps are:

```bash
./gradlew build
```

The `gradlew` wrapper downloads the correct Gradle version if needed.

## License

All source code in this repository is released under the "All Rights Reserved"
license as noted in the `LICENSE` file and `mods.toml`.
