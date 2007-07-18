/**
 * Component rendering peer: TextComponent
 */
EchoRender.ComponentSync.TextComponent = function() { };

EchoRender.ComponentSync.TextComponent.prototype = new EchoRender.ComponentSync;

EchoRender.ComponentSync.TextComponent.prototype._renderStyle = function() {
    EchoRender.Property.Border.render(this.component.getRenderProperty("border"), this._textComponentElement);
    EchoRender.Property.Color.renderFB(this.component, this._textComponentElement);
    EchoRender.Property.Font.renderComponentProperty(this.component, "font", null, this._textComponentElement);
    EchoRender.Property.Insets.renderComponentProperty(this.component, "insets", null, this._textComponentElement, "padding");
    var width = this.component.getRenderProperty("width");
    if (width) {
    	this._textComponentElement.style.width = width.toString();
    }
    var height = this.component.getRenderProperty("height");
    if (height) {
    	this._textComponentElement.style.height = height.toString();
    }
};

EchoRender.ComponentSync.TextComponent.prototype._addEventHandlers = function() {
    EchoWebCore.EventProcessor.add(this._textComponentElement, "blur", new EchoCore.MethodRef(this, this._processBlur), false);
    EchoWebCore.EventProcessor.add(this._textComponentElement, "keyup", new EchoCore.MethodRef(this, this._processKeyUp), false);
};

EchoRender.ComponentSync.TextComponent.prototype.renderDispose = function(update) {
    EchoWebCore.EventProcessor.removeAll(this._textComponentElement);
    this._textComponentElement = null;
};

EchoRender.ComponentSync.TextComponent.prototype._processBlur = function(e) {
    if (!this.component.isActive()) {
        return;
    }
    this._sanitizeInput();
    this.component.setProperty("text", e.registeredTarget.value);
};

EchoRender.ComponentSync.TextComponent.prototype._processKeyUp = function(e) {
    if (!this.component.isActive()) {
		EchoWebCore.DOM.preventEventDefault(e);
        return;
    }
    this._sanitizeInput();
    this.component.setProperty("text", e.registeredTarget.value);
    if (e.keyCode == 13) {
	    this.component.fireEvent(new EchoCore.Event(this.component, "action"));
    }
};

EchoRender.ComponentSync.TextComponent.prototype.renderUpdate = function(update) {
    var element = this._textComponentElement;
    var containerElement = element.parentNode;
    this.renderDispose(update);
    containerElement.removeChild(element);
    this.renderAdd(update, containerElement);
    return false; // Child elements not supported: safe to return false.
};

EchoRender.ComponentSync.TextComponent.prototype._sanitizeInput = function() {
    var maximumLength = this.component.getRenderProperty("maximumLength", -1);
    if (maximumLength >= 0) {
        if (this._textComponentElement.value && this._textComponentElement.value.length > maximumLength) {
            this._textComponentElement.value = this._textComponentElement.value.substring(0, maximumLength);
        }
    }
};

/**
 * Component rendering peer: TextArea
 */
EchoRender.ComponentSync.TextArea = function() { };

EchoRender.ComponentSync.TextArea.prototype = new EchoRender.ComponentSync.TextComponent;

EchoRender.ComponentSync.TextArea.prototype.renderAdd = function(update, parentElement) {
    this._textComponentElement = document.createElement("textarea");
    this._renderStyle(this._textComponentElement);
    this._textComponentElement.style.overflow = "auto";
    this._addEventHandlers(this._textComponentElement);
    if (this.component.getProperty("text")) {
        this._textComponentElement.appendChild(document.createTextNode(this.component.getProperty("text")));
    } else {
        this._textComponentElement.appendChild(document.createTextNode(""));
    }
    parentElement.appendChild(this._textComponentElement);
};

/**
 * Component rendering peer: TextField
 */
EchoRender.ComponentSync.TextField = function() {
	this._type = "text";
};

EchoRender.ComponentSync.TextField.prototype = new EchoRender.ComponentSync.TextComponent;

EchoRender.ComponentSync.TextField.prototype.renderAdd = function(update, parentElement) {
    this._textComponentElement = document.createElement("input");
    this._textComponentElement.setAttribute("type", this._type);
    var maximumLength = this.component.getRenderProperty("maximumLength", -1);
    if (maximumLength >= 0) {
	    this._textComponentElement.setAttribute("maxlength", maximumLength);
    }
    this._renderStyle(this._textComponentElement);
    this._addEventHandlers(this._textComponentElement);
    if (this.component.getProperty("text")) {
        this._textComponentElement.setAttribute("value", this.component.getProperty("text"));
    }
    parentElement.appendChild(this._textComponentElement);
};

EchoRender.ComponentSync.TextField.prototype._sanitizeInput = function() {
	// allow all input
};

/**
 * Component rendering peer: PasswordField
 */
EchoRender.ComponentSync.PasswordField = function() {
	this._type = "password";
};

EchoRender.ComponentSync.PasswordField.prototype = new EchoRender.ComponentSync.TextField;

EchoRender.registerPeer("TextArea", EchoRender.ComponentSync.TextArea);
EchoRender.registerPeer("TextField", EchoRender.ComponentSync.TextField);
EchoRender.registerPeer("PasswordField", EchoRender.ComponentSync.PasswordField);
