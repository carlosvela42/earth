var ImageViewer = function() {
    documents = [];
    currentDocument = null;
    documentPath = null;

    this.zoomDefault = 0.1;
    this.panDefault = -0.1;

    existUser = false;
    layerActive = null;
    annotest = null;
    // Initialize A New Annotation by type is chosen on the Popup option.
    // Line,Rectangle,ecllipse.
    settingAnno = null;
    // Save Latest Annotation has just been drawn.
    drawAnno = null;
    // Save selected annotation from screen.
    selectedAnnos = [];
    // Save an annotation after processing copy or cut.
    clipBoardGroup = null;
    clipBoardAnnos = [];

    // Rect template for Selecting one or mutiple svg elements.
    sMultipleSvg = null;

    allAnnos = [];

    // Transform info.
    currentRotation = 0;
    copyTimeRotation = 0;
    currentScale = 1;
    //	cTranslateTop=0;
    //	cTranslateLeft=0;

    // Contain all the contents of image document.
    svg = null;
    svgStartLeft = 0;
    svgStartTop = 0;
    svgId = null;

    imgWidth = null;
    imgHeight = null;

    drawStartX = null;
    drawStartY = null;

    selectedGroup = null;
    hiddenRect = null;
    this.toolbarFlg = toolbars.BTN_SELECT;
    copyCutFlg = false;
    drawFlg = false;
    selectedFlg = false;
    


    //	Array.prototype.insert = function(index, item) {
    //	    this.splice(index, 0,item);
    //	};
};


var waitMouseUpFlg = false;
var deleteLayerFlag = true;


var ctrlDown = false;
var CTRLKEY = 17;
var CMDKEY = 91;
var VKEY = 86;
var CKEY = 67;
var clickOnSelectedBound = false;
var layerNoMax = 0;
var contrast = 100;
var brightness = 100;
var checkGrayscale = true;
var tmpContrast = 100;
var tmpBrightness = 100;
var tmpCheckGrayscale = true;
var xGrayscale = null;
var filterChild = null;
var userInfo = window.userId;
var templateLayerName = "TMP5";
var commentImage = "resources/images/imageViewer/rsz_commenticon.jpg";
var cursorImage = "resources/images/imageViewer/Pen-50.png";

var result = {
    SUCCESS: 1,
    ERROR: 0
};

var documentType = {
    IMAGE: "image",
    PDF: "pdf",
    TIFF: "tiff"
};

var rotateType = {
    RIGHT: 1,
    LEFT: -1
};

var propertiesType = {
    penColor: "Black",
    fillColor: "none",
    highlightColor: "yellow",
    lineSize: 1,
    fontFamily: "Times New Roman",
    fontStyle: "Regular",
    fontSize: "18"
};

var toolbars = {
    BTN_SELECT: "#select",
    BTN_ZOOMFP: "#zoomFullPage",
    BTN_ZOOMFW: "#zoomFullWidth",
    BTN_ZOOM200: "#zoom200",
    BTN_ZOOM100: "#zoom100",
    BTN_ZOOM75: "#zoom75",
    BTN_ZOOM50: "#zoom50",
    BTN_ZOOMIN: "#zoomin",
    BTN_ZOOMOUT: "#zoomout",
    BTN_ROTATE_LEFT: "#rotateC",
    BTN_ROTATE_RIGHT: "#rotate",
    BTN_COPY: "#copy",
    BTN_CUT: "#cut",
    BTN_PASTE: "#paste",
    BTN_PROPERTIES: "#properties",
    BTN_OKPROPERTIES: "#okProperties",
    BTN_CANCELPROPERTIES: "#cancelProperties",
    BTN_TEXTPROPERTIES: "#textProperties",
    BTN_COMMENTPROPERTIES: "#commentProperties",
    BTN_CANCELCOMMENTPROPERTIES: "#cancelCommentProperties",
    BTN_PRINTALL: "#print",
    BTN_PRINTIMAGE: "#print0",
    BTN_LAYER: "#btnLayer",
    BTN_LAST: "#last",
    BTN_NEXT: "#next",
    BTN_PREVIOUS: "#previous",
    BTN_FIRST: "#first",
    BTN_ADDLAYER: "#addLayer",
    BTN_REMOVELAYER: "#removeLayer",
    BTN_RENAMELAYER: "#renameLayer",
    BTN_ACTIVELAYER: "#activeLayer",
    BTN_DISPLAYLAYER: "#displayLayer",
    BTN_OKLAYER: "#okLayer",
    BTN_OKLAYERDELETE: "#okLayerDelete",
    BTN_GRAYSCALE: "#btnGrayscale",
    BTN_OKGRAYSCALE: "#okGrayscale",
    BTN_CANCELGRAYSCALE: "#cancelGrayscale",
    BTN_CONTROLS: "#controls",
    BTN_CONTROLS1: "#controls1",
    BTN_CBOX1: "#cbox1",
    BTN_SAVEIMAGE: "#saveImage",
    BTN_SUBIMAGE: "#subImage"
};

var textExample = {
    COMMENTTEXT: "#commentText",
    FONTSTYLEID: "#fontStyle",
    INPUTCOLOR: "input[name=color]:checked",
    INPUTCOLOR1: "input[name=color]",
    INPUTFILL: "input[name=fill]:checked",
    INPUTHIGHLIGHT: "input[name=highlight]:checked",
    WIDTH: '#width',
    COLOR: '#color',
    COLORSELECT: '#color option:selected'
}

var commentExample = {
    COMMENTTEXTBOX: "#tbComment",
    COMMENTTEXTAREA: '#commentTxtArea',
    COMMENTPOPUP: "#myModalComment",
    COMMENTCLASS: "newComment"
}

var annoTypes = {
    LINE: "#line",
    RECTANGLE: "#rectangle",
    TEXT: "#text",
    HIGHLIGHT: "#highlight",
    COMMENT: "#comment"
};