package model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ObjectType {
    BRICK_BLUE("/cube.obj", "src/textures/blocks/brick_blue.png", 0.2f),
    BRICK_GRAY("/cube.obj", "src/textures/blocks/brick_gray.png", 0.2f),
    BRICK_GREEN("/cube.obj", "src/textures/blocks/brick_green.png", 0.3f),
    BRICK_LIGHTGREEN("/cube.obj", "src/textures/blocks/brick_lightgreen.png", 0.2f),
    BRICK_PURPLE("/cube.obj", "src/textures/blocks/brick_purple.png", 0.2f),
    BRICK_RED("/cube.obj", "src/textures/blocks/brick_red.png", 0.2f),
    BRICK_YELLOW("/cube.obj", "src/textures/blocks/brick_yellow.png", 0.2f),
    DIRT("/cube.obj", "src/textures/blocks/dirt.png", 0.1f),
    GRASS("/cube.obj", "src/textures/blocks/grass.png", 0.1f),
    GRASS_DIRT("/cube.obj", "src/textures/blocks/grass_dirt.png", 0.1f),
    GRASS_SNOW("/cube.obj", "src/textures/blocks/grass_snow.png", 2f),
    ICE("/cube.obj", "src/textures/blocks/ice.png", 10f),
    LAVA("/cube.obj", "src/textures/blocks/lava.png", 0f),
    SNOW("/cube.obj", "src/textures/blocks/snow.png", 2f),
    STONE("/cube.obj", "src/textures/blocks/stone.png", 0.2f),
    STONE_DARKGRAY("/cube.obj", "src/textures/blocks/stone_darkgray.png", 0.2f),
    STONE_LIGHTGRAY("/cube.obj", "src/textures/blocks/stone_lightgray.png", 0.2f),
    WATER("/cube.obj", "src/textures/blocks/water.png", 10f);

    private final String objectFileName;
    private final String textureFileName;
    private final float reflectance;
}
