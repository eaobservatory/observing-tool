package ODBServerPackage;

public final class ProgKeyHolder implements java.io.Serializable, java.lang.Cloneable
{
    //	instance variable 
    public ProgKey value;

    //	constructors 
    public ProgKeyHolder() {
	this(null);
    }

    public ProgKeyHolder(ProgKey __arg) {
	value = __arg;
    }

}
