public class Task
{
    private String name;
    private String description;
    private boolean complete;

    public Task(String n, String d)
    {
        name = n;
        description = d;
        complete = false;
    }

    public String toString()
    {
        String rVal = name+"\n"+description+"\n"+"Complete? "+complete+"\n";
        return rVal;
    }

    public String getName()
    {
        return name;
    }

    public void markComplete()
    {
        complete = true;
    }

    public void markIncomplete()
    {
        complete = false;
    }

    public Boolean isComplete()
    {
        return complete;
    }
}