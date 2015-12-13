public class MyVertex {
	private static int IDCOUNTER = 0;
    protected String name;
    private final int id;

    MyVertex(String name)
    {
        this.name = name;
        this.id = IDCOUNTER++;
    }
    MyVertex(int id,String name)
    {
        this.name = name;
        this.id = id;
        if(IDCOUNTER <= id)
        {
        	IDCOUNTER = id+1;
        }
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
    Integer getId()
    {
    	return id;
    }
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
    static void resetIdCounter(int idcounter)
    {
    	IDCOUNTER = idcounter;
    }
}