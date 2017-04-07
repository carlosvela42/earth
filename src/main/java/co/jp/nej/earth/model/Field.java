package co.jp.nej.earth.model;

public class Field {
    private String name;
    private String description;
    private int type;
    private int size;
    private boolean required;
    private boolean encrypted;
    
    /**
     * 
     */
    public Field() {
        super();
    }

    /**
     * @param name
     * @param description
     * @param type
     * @param size
     * @param required
     * @param encrypted
     */
    public Field(String name, String description, int type, int size, boolean required, boolean encrypted) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.size = size;
        this.required = required;
        this.encrypted = encrypted;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return the encrypted
     */
    public boolean isEncrypted() {
        return encrypted;
    }

    /**
     * @param encrypted the encrypted to set
     */
    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
