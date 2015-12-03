public class MyVertex {
	private static int IDCOUNTER = 0;
    protected String name;
    private final int id;

    MyVertex(String name)
    {
        this.name = name;
        this.id = IDCOUNTER++;
    }
    String getToolTip()
    {
    	return null;
    }
    @Override
    public String toString()
    {
        return name;
    }
//    @Override
//    public int hashCode()
//    {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + id;
//        result = prime * result + ((name == null) ? 0 : name.hashCode());
//        return result;
//    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MyVertex other = (MyVertex) obj;
        if (id != other.id)
            return false;
        return true;
    }

    }