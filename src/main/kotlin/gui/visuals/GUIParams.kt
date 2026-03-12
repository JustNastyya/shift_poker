package gui

/**
 * a class which stores params for the gui
 * is used to avoid using numbers for positions of elements on the screen
 */
data class GUIParams(
    // window params
    val windowWidth: Int = 1920,
    val windowHeight: Int = 1080,
    val centerX: Int = 960,
    val centerY: Int = 540,

    // global gui elements params
    val titleWidth: Int = 800,
    val titleHeight: Int = 100,
    val textFieldWidth: Int = 420,
    val textFieldHeight: Int = 65,
    val textFieldSpace: Int = 20,
    val buttonWidth: Int = 260,
    val buttonHeight: Int = 60,
    val comboWidth: Int = 300,
    val comboHeight: Int = 50,

    // game gui elem params
    val logsWidth: Int = 500,
    val logsHeight: Int = 270,
    val cardHeight: Int = 200,
    val cardWidth: Int = 130,
    val cardSpace: Int = 20,

    // colors
    )
