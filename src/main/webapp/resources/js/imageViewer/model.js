var ImageViewer = function(){
	documents = [];
	currentDocument = null;
	documentPath = null;

	// Initialize A New Annotation by type is chosen on the Popup option.
	// Line,Rectangle,ecllipse.
	settingAnno = null;
	// Save Latest Annotation has just been drawn.
	drawAnno = null;
	// Save selected annotation from screen.
	selectedAnno = null;
	// Save an annotation after processing copy or cut.
	clipBoardAnno = null;
	
	allAnnos = [];

	currentRotation = 0;
	currentScale = 1;
	cTranslateTop=0;
	cTranslateLeft=0;
	
	// Contain all the contents of image document.
	svg = null;

	svgStartLeft = 0;
	svgStartTop = 0;
	svgId = null;
	
	imgWidth=null;
	imgHeight=null;
};

var result = {
	SUCCESS : 1,
	ERROR : 0
};

var documentType = {
	IMAGE : "1",
	PDF : "2",
	TIFF : "3"
};

var rotateType = {
	RIGHT : 1,
	LEFT : -1
};

var toolbars = {
	BTN_SELECT : "#select"
	,BTN_ZOOMIN : "#zoomin"
	,BTN_ZOOMOUT : "#zoomout"
	,BTN_ROTATE_LEFT : "#rotateC"
	,BTN_ROTATE_RIGHT : "#rotate"
};

var annoTypes = {
	LINE : "#line"
	,RECTANGLE : "#rectangle"
	,TEXT : "#text"
	,HIGHLIGHT : "#highlight"
	,COMMENT : "#comment"
};

var Document = {
	workitemId:null
    ,folderItemNo:null
    ,documentNo:null
    ,mgrTemplate:null
    ,pageCount:null
    ,viewInformation:null
    ,documentType:null
    ,action:null
    ,documentData:null
    ,documentPath:null
    ,documentBinary:null
    ,templateId:null
    ,layers:[]
}

var Layer = {
	workitemId:null
    ,folderItemNo:null
    ,documentNo:null
    ,layerNo:null
    ,mgrTemplate:null
    ,ownerId:null
    ,action:null
    ,layerData:null
    ,annotations:null
    ,templateId:null
}