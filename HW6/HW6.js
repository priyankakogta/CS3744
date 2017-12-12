/*
 * Homework 6
 *
 * @author Priyanka Kogta
 * @version 1
 */

/////////////////////////////
// MODEL - begin
/////////////////////////////
var projectName;      // The name of the light configuration.
var columnCount;      // The number of columns in the light grid.
var rowCount;         // The number of rows in the light grid.
var backgroundColor;  // The background color of the light grid.
var fileData;         // The array of light data from the file.
var maxTime;          // The largest time value from the file.
var title;            // the window title.

// Creates a model with default values and no lights.
function initModel() {
    fileData = new Array();
    clearModel();
}

// Sets the default values in the model.
function clearModel() {
    projectName = null;
    columnCount = 0;
    rowCount = 0;
    backgroundColor = "rgba(0,0,0,1)"; // black
    fileData.length = 0;
    maxTime = 0;
    title = "HW6: prikogta";
}

// Returns the color of a light given the color components (rgba).
// r Red component (between 0 and 100)
// g Green component (between 0 and 100)
// b Blue component (between 0 and 100)
// a Opacity component (between 0 and 100)
// return String in the format "rgba(red, green, blue, opacity)"
function convertColor(r, g, b, a) {
    return "rgba("+Math.round(2.55*r)+" ,"+Math.round(2.55*g)+" ,"+Math.round(2.55*b)+" ,"+Math.round(a/100.0)+")";
}
/////////////////////////////
// MODEL - end
/////////////////////////////

/////////////////////////////
// VIEW - begin
/////////////////////////////
// Assembles the view from the elements and initializes the view.
function initView() {
    setTitle(); // Sets the default window title and overides HTML title tag (if present).
    d3.select("body")
        .append("div")
        .attr("id", "gui"); // Creates a container for all input GUI elements (menu and slider).
    var menuBar = d3.select("#gui")
        .append("div")
        .attr("id", "menuBar") // Creates the menu bar (div).
    var fileMenu = menuBar.append("div")
        .attr("id", "fileMenu")
        .attr("class", "menu"); // Creates the File menu container (div).
    fileMenu.append("button")
        .html("File")
        .attr("id", "fileButton")
        .on("click", showFile); // Creates the File menu button to show the pull-down menu (button).
    var fileMenuList = fileMenu.append("div")
        .attr("id", "fileMenuList")
        .attr("class", "menulist"); // Creates the File menu pull-down menu (div).
    fileMenuList.append("a")
        .attr("id", "openMenuItem")
        .on("click", showOpen)
        .html("Open ^O"); // Creates the File menu Open menu item (a).
    fileMenuList.append("a")
        .attr("id", "closeMenuItem")
        .on("click", showClose)
        .html("Close ^W"); // Creates the File menu Close menu item (a).
    fileMenuList.append("hr"); // Creates the File menu separator (hr).
    fileMenuList.append("a")
        .on("click", showExit)
        .html("Exit ^X"); // Creates the File menu Exit menu item (a).
    var helpMenu = menuBar.append("div")
        .attr("id", "helpMenu")
        .attr("class", "menu"); // Creates the Help menu container (div).
    helpMenu.append("button")
        .html("Help")
        .attr("id", "helpButton")
        .on("click", showHelp); // Creates the Help menu button to show the pull-down menu (button).
    helpMenu.append("div")
        .attr("id", "helpMenuList")
        .attr("class", "menulist") // Creates the Help menu pull-down menu (div).
        .append("a")
        .on("click", showAbout)
        .html("About ^A"); // Creates the Help menu About menu item (a).
    d3.select("#gui")
        .append("input")
        .attr("type", "range")
        .attr("id", "slider")
        .attr("min", "0")
        .attr("max", "100")
        .attr("value", "0")
        .attr("step", "any")
        .attr("list", "tickMarks")
        .on("input",function() {
            draw(this.value);
        })
        .on("change",function() {
            draw(this.value);
        });                       // Creates the slider (input range).
    d3.select("#gui")
        .append("div")
        .attr("id", "labels"); // Creates the container for the slider labels (div).
    d3.select("#labels")
        .selectAll("div")
        .data(Array.apply(null, {length: 11}).map(Number.call, Number)) // Creates an array of numbers from 0 to 11.
        .enter()
        .append("div")
        .style("position", "absolute")
        .style("left", function(i) {
            return labelPosition(i);
        })
        .html(function(i) {
            return 10 * i;
        });                      // Creates the slider labels (div).
    var canvas = d3.select("body")
        .append("canvas")
        .attr("id", "canvas"); // Creates the drawing area (canvas).
    fileChooser = d3.select("#gui")
        .append("input")
        .attr("type", "file")
        .attr("id", "fileChooser")
        .style("display", "none")
        .on("change", fileSelect); // Creates the file select box (input file) - not visible.
    d3.select("#gui")
        .append("datalist")
        .attr("id", "tickMarks")
        .selectAll("option")
        .data(Array.apply(null, {length: 101}).map(Number.call, Number)) // Creates an array of numbers from 0 to 100.
        .enter()
        .append("option")
        .attr("value", function(i) {
            return i;
        });                      // Creates the slider tick marks (option).
    d3.select("body")
        .on("keydown", function() {
            var event = d3.event;
            if (event.getModifierState("Control") || event.getModifierState("Alt")) {
                switch (event.key.toLowerCase()) {
                    case "a": // using letter A will display About
                        showAbout();
                        event.preventDefault();
                        break;
                    case "o":  // using letter O will open page
                        showOpen();
                        event.preventDefault();
                        break;
                    case "w": // using letter W will close GUI display
                        showClose();
                        event.preventDefault();
                        break;
                    case "x": // using letter X will exit the window
                        showExit();
                        event.preventDefault();
                        break;
                }
            }
        })                      // Creates the key accelerators.
        .on("click",function() {
            var id = d3.event.target.id;
            if (id != "helpButton" && id != "fileButton") {
                clearMenus();
            }
        });                     // Closes menus if click occurs outside menus.
    resetView(); // Sets the initial view state.
    d3.select(window).on("resize", windowResize); // Sets the event handler for resize events.
    d3.select(window).node().dispatchEvent(new Event("resize")); // Triggers a resize event.
    glInit(); // Initializes WebGL data.
}

// Resets the slider value and enables/disables File menu items.
function resetView() {
    d3.select("#slider").property("value", "0");
    d3.select("#canvas").style("background-color", backgroundColor);
    disableMenuItem("openMenuItem", false);
    disableMenuItem("closeMenuItem", true);
}

// Removes the lights and resets the view / title.
function clearView() {
    d3.select("#canvas").html("");
    resetView();
    setTitle();
}

// Updates the window title.
function setTitle() {
    document.title = title + (projectName ? " - " + projectName : "");
}

// Disables/enables the menu item.
// id The element id
// flag If true disable else enable
function disableMenuItem(id, flag) {
    d3.select("#" + id).classed("disable", flag);
}

// Displays all the lights that have time values less than or equal the percentage of the maximum time.
// t Slider value (between 0 and 100)
function draw(t) {
    time = t / 100 * maxTime; // Calculates the actual time.
    glUpdate(time); // Updates GL data.
}

// Returns the cell id.
// r Row index
// c Column index
// return The string representing the cell
function cellId(r, c) {
    return "cell" + (r * rowCount + c);
}

// Returns the cell column index.
// id Cell id
// return Cell column index
function cellColumn(id) {
    return id.replace(/cell/, "") - rowCount * cellRow(id);
}

// Returns the cell row index.
// id Cell id
// return Cell row index
function cellRow(id) {
    return Math.floor(id.replace(/cell/, "") / rowCount);
}

// Returns the label position.
// i Label index
// return Label position
function labelPosition(i) {
    return 5 + i * (d3.select("#labels").style("width").replace(/px/,"") - 25) / 10 + "px";
}

// Returns the left position of a dialog.
// d id
// return Left position
function OKDialogLeft(d) {
    return (window.innerWidth - d3.select("#" + d).style("width").replace(/px/,"")) / 2 + "px";
}

// Returns the top position of a dialog.
// d id
// return Top position
function OKDialogTop(d) {
    return (window.innerHeight - d3.select("#" + d).style("height").replace(/px/,"")) / 2 + "px";
}

/////////////////////////////
// VIEW - end
/////////////////////////////

/////////////////////////////
// CONTROLLER - begin
/////////////////////////////
// Initializes the web page.
function init() {
    initModel();
    initView();
}

// Shows the File menu.
function showFile() {
    clearMenus();
    d3.select("#fileMenuList").classed("show", !d3.select("#fileMenuList").classed("show"));
}

// Shows the file select box if the Open menu item is enabled.
function showOpen() {
    if (!d3.select("#openMenuItem").classed("disable")) {
        clearMenus();
        d3.select("#fileChooser").node().dispatchEvent(new MouseEvent("click"));
    }
}

// Resets to the initial, empty model.
function showClose() {
    if (!d3.select("#closeMenuItem").classed("disable")) {
        clearMenus();
        clearModel();
        clearView();
        clearGL();
    }
}

// Clears the document (empty page).
function showExit() {
    clearMenus();
    document.write("");
}

// Shows the Help menu.
function showHelp() {
    clearMenus();
    d3.select("#helpMenuList").classed("show", !d3.select("#helpMenuList").classed("show"));
}

// Shows a dialog with info about the web page.
function showAbout() {
    clearMenus();
    d3.select("body")
        .append("div")
        .attr("id", "modal")
        .append("div")
        .attr("id", "dialog")
        .html("<H4>About Homework 6</H4><HR><P>Homework 6 solution.</P>")
        .append("button")
        .attr("id", "okButton")
        .html("OK")
        .on("click", showAboutOK);
    windowResize();
}

// Closes the dialog.
function showAboutOK() {
    d3.select("#modal").remove();
}

// Hides the pull-down menus.
function clearMenus() {
    d3.selectAll(".menulist").classed("show", false);
}

// Loads the file, populates the model and draws.
function fileSelect() {
    var file = document.getElementById("fileChooser").files[0];
    if (file) { // File selected.
        var fileReader = new FileReader();
        fileReader.onload = function() { // used to be function(progressEvent)
            var numbers = null;
            d3.csvParseRows(this.result, function(row, i) {
                switch (i) {
                    case 0:
                        projectName = row;
                        break;
                    case 1:
                        numbers = row.map(Number);
                        rowCount = parseInt(numbers[0]);
                        columnCount = parseInt(numbers[1]);
                        break;
                    case 2:
                        numbers = row.map(Number);
                        backgroundColor = convertColor(numbers[0], numbers[1], numbers[2], numbers[3]);
                        break;
                    default:
                        numbers = row.map(Number);
                        if (numbers.length == 6) { // Sets time to 0 for the initial lights.
                            numbers.push(0);
                            fileData.push(numbers);
                        }
                        else if (numbers.length == 7) {
                            fileData.push(numbers);
                        }
                }
            });
            fileData.sort(function(a, b) { // Sorts the lights based on times.
                return a[6] - b[6];
            });
            maxTime = fileData[fileData.length - 1][6]; // Gets the maximum time (the last element of the sorted array).
            d3.select("#slider").property("value", "0");
            setTitle();
            draw(d3.select("#slider").attr("value"));
        };
        fileReader.readAsText(file);
        d3.select("#fileChooser").property("value", "");
        disableMenuItem("closeMenuItem", false);
        disableMenuItem("openMenuItem", true);
    }
}

// Updates the canvas height when the window resizes.
function windowResize() {
    var size = window.innerHeight;
    size -= d3.select("body").style("margin-top").replace(/px/,"");
    size -= d3.select("body").style("margin-bottom").replace(/px/,"");
    size -= d3.select("#gui").style("height").replace(/px/,"");
    size -= d3.select("#gui").style("margin-top").replace(/px/,"");
    size -= d3.select("#gui").style("margin-bottom").replace(/px/,"");
    d3.select("#canvas").style("height", size + "px"); // Sets the canvas height.
    var bodyWidth = window.innerWidth - d3.select("body").style("margin-left").replace(/px/,"");
    bodyWidth -= d3.select("body").style("margin-right").replace(/px/,"");
    size = bodyWidth;
    size -= d3.select("#canvas").style("margin-left").replace(/px/,"");
    size -= d3.select("#canvas").style("margin-right").replace(/px/,"");
    d3.select("#canvas").style("width", size + "px"); // Sets the canvas width.
    size = bodyWidth;
    size -= d3.select("#slider").style("margin-left").replace(/px/,"");
    size -= d3.select("#slider").style("margin-right").replace(/px/,"");
    d3.select("#slider").style("width", size + "px"); // Sets the slider width.
    size = bodyWidth;
    size -= d3.select("#labels").style("margin-left").replace(/px/,"");
    size -= d3.select("#labels").style("margin-right").replace(/px/,"");
    d3.select("#labels").style("width", size + "px") // Sets the labels width.
        .selectAll("div")
        .data(Array.apply(null, {length: 11}).map(Number.call, Number))
        .style("left", function(i) {
            return labelPosition(i);
        }); // Positions the labels.
    if (!d3.select("#dialog").empty()) {
        d3.select("#dialog")
            .style("left", (window.innerWidth - d3.select("#dialog").style("width").replace(/px/,"")) / 2 + "px")
            .style("top", (window.innerHeight - d3.select("#dialog").style("height").replace(/px/,"")) / 2 + "px");
    } // Positions the dialog.
   // glResize();
   resizeGrid();
}
/////////////////////////////
// CONTROLLER - end
/////////////////////////////

/////////////////////////////
// WebGL - start
/////////////////////////////
var canvas;
var gl;
var verticesBuffer;
var shaderProgram;
var vertexPositionAttribute;
var transform;
var color;
var vertices;

// Updates WebGL data.
function glUpdate(time) {
        vertices = null;
    if (rowCount >= 0 && columnCount >= 0) {
        var widthOfCell = 2 / columnCount; // in notes to be divided by 2 for width
        var heightOfCell = 2 / rowCount; // divide 2 by total row Count
        var index = 0;
        cCount = fileData.length; // depends on what length of file is in csv
        for (i = 0; i < fileData.length; i++) {
            if (fileData[i][6] > time) {
                cCount = i;
                break;
            }
        }
        vertices = new Float32Array(4*(rowCount + columnCount + 2 + 2 * cCount)); // array is being initialized
        // Creates the horizontal lines
        for (var i = 0; i < rowCount + 1; i++) {
        vertices[index++] = -1;
        vertices[index++] = 1 - i * heightOfCell;
        vertices[index++] = 1;
        vertices[index++] = 1 - i * heightOfCell;
        }

        // Creates the vertical lines for the grid
        for (var i = 0; i < columnCount + 1; i++) {
            vertices[index++] = -1 + i * widthOfCell; // be careful with 1 and i, because you'll get diagonal lines if you wrote 1!!
            vertices[index++] = -1;
            vertices[index++] = -1 + i * widthOfCell;
            vertices[index++] = 1;
        }
        // Creates the vertices for the lights. Use Slide 20 for reference!
        for (var i = 0; i < cCount; i++) {
            vertices[index++] = -1 + fileData[i][1] * widthOfCell;
            vertices[index++] = 1 - (fileData[i][0] + 1) * heightOfCell;
            vertices[index++] = -1 + (fileData[i][1] + 1) * widthOfCell;
            vertices[index++] = 1 - (fileData[i][0] + 1) * heightOfCell;
            vertices[index++] = -1 + fileData[i][1] * widthOfCell;
            vertices[index++] = 1 - fileData[i][0] * heightOfCell;
            vertices[index++] = -1 + (fileData[i][1] + 1) * widthOfCell;
            vertices[index++] = 1 - fileData[i][0] * heightOfCell;
        }
        }
        gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
        gl.vertexAttribPointer(vertexPositionAttribute, 2, gl.FLOAT, false, 0, 0);
        glDraw();
}

// Initializes WebGL data.
function glInit() {
    canvas = d3.select("#canvas");
    gl = null;
    try {
        gl = canvas.node().getContext("webgl") || canvas.node().getContext("experimental-webgl");
    }
    catch (e) {
    }
    if (gl) {
        initShaders();
        initBuffers();
        glResize();
    }
    clearGL();
}

// Initializes the buffers.
function initBuffers() {
    vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "vertexPosition");
    gl.enableVertexAttribArray(vertexPositionAttribute);
    verticesBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, verticesBuffer);
}

// Resizes the canvas to fit the window.
function glResize() {
    if (gl) {
        d3.select("#canvas")
            .attr("width", d3.select("#canvas").style("width").replace(/px/, ""))
            .attr("height", d3.select("#canvas").style("height").replace(/px/, ""));
        gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight);
    }
    glDraw();
}

// Draws the lights.
function glDraw() {
    if (gl) {
        var backgroundColour = backgroundColor.replace(/rgba\(/, "").replace(/\)/,"").replace(/ /, "").split(",");
        // Background colour is set.
        gl.clearColor(backgroundColour[0] / 255, backgroundColour[1] / 255, backgroundColour[2] / 255, parseFloat(backgroundColour[3]));
        gl.clear(gl.COLOR_BUFFER_BIT);
        if (vertices != null) { // else it won't draw anything
            gl.bindBuffer(gl.ARRAY_BUFFER, verticesBuffer);
            gl.vertexAttribPointer(vertexPositionAttribute, 2, gl.FLOAT, false, 0, 0);
            color = gl.getUniformLocation(shaderProgram, "color");
           // uniform4f specifies the values of uniform variables.
            gl.uniform4f(color, 0.0, 0.0, 1.0, 1.0); // makes grid lines blue
            // creates the grid
            gl.drawArrays(gl.LINES, 0, 2 * (rowCount + columnCount + 2));
            for (i = 0; i < cCount; i++) {
                gl.uniform4f(color, fileData[i][2] / 100, fileData[i][3] / 100, fileData[i][4] / 100, fileData[i][5] / 100);
                gl.drawArrays(gl.TRIANGLE_STRIP, 2 * (rowCount + columnCount + 2) + 4 * i, 4); // from slide 24 to use TRIANGLE_STRIP
            }
        }
    }
}

function clearGL() { // clears the page.
    vertices = null;
    glDraw();
}

// Compiles shaders and uploads them to the graphics card.
function initShaders() {
    var fragmentShader = getShader(gl, "fragment-shader");
    var vertexShader = getShader(gl, "vertex-shader");
    shaderProgram = gl.createProgram();
    gl.attachShader(shaderProgram, vertexShader);
    gl.attachShader(shaderProgram, fragmentShader);
    gl.linkProgram(shaderProgram);
    if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
        alert("Unable to initialize the shader program.");
    }
    gl.useProgram(shaderProgram);
}

// Creates the shader.
// gl The WebGL context.
// id The shader id.
function getShader(gl, id) {
    var shaderScript = document.getElementById(id);
    if (!shaderScript) {
        return null;
    }
    var theSource = "";
    var currentChild = shaderScript.firstChild;
    while(currentChild) {
        if (currentChild.nodeType == 3) {
            theSource += currentChild.textContent;
        }
        currentChild = currentChild.nextSibling;
    }
    var shader;
    if (shaderScript.type == "x-shader/x-fragment") {
        shader = gl.createShader(gl.FRAGMENT_SHADER);
    } else if (shaderScript.type == "x-shader/x-vertex") {
        shader = gl.createShader(gl.VERTEX_SHADER);
    } else {
        return null;
    }
    gl.shaderSource(shader, theSource);
    gl.compileShader(shader);
    if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
        alert("An error occurred compiling the shaders: " + gl.getShaderInfoLog(shader));
        return null;
    }
    return shader;
}
/////////////////////////////
// WebGL - end
/////////////////////////////

window.onload = init; // Registers the event handler for loading the page.
