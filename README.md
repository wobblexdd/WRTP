# WobbleRTP

WobbleRTP is a random teleport plugin designed for SMP servers. It provides safe location generation, cooldown control, and integration with player settings without modifying external plugins.

## Features
- Random teleport command (/rtp)
- Safe location detection (no lava, no air, no unsafe blocks)
- Configurable radius (min / max)
- Cooldown system
- Teleport delay with cancel-on-move
- World restrictions
- Biome blacklist support
- Retry system with max attempts
- Integration with WobbleSettings

## Command
/rtp

## Configuration

rtp:
  min-radius: 500
  max-radius: 3000
  cooldown: 60
  delay: 3
  cancel-on-move: true
  max-attempts: 50

  disabled-biomes:
    - OCEAN
    - DEEP_OCEAN

  enabled-worlds:
    - world

messages:
  no-permission: "<red>No permission"
  cooldown: "<red>Wait {time}s"
  searching: "<gray>Searching location..."
  teleporting: "<green>Teleporting..."
  failed: "<red>Failed to find safe location"

## How it works
The plugin generates random X/Z coordinates within the configured radius, finds the highest valid Y level, and validates the location before teleporting the player. If the location is unsafe, it retries until a valid position is found or the max attempts limit is reached.

## Integration
WobbleRTP can integrate with WobbleSettings:
- Respects player settings (e.g. teleport restrictions)
- Does not modify Essentials or other plugins

## Build
mvn clean package

## Notes
- Designed for performance and stability
- Does not require external dependencies
- Safe for production SMP environments
