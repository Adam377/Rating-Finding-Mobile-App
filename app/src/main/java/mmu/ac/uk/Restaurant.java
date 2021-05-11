package mmu.ac.uk;

public class Restaurant
{
    //members
    private String name;
    private int hygieneRating;

    //constructor


    //methods
    public String getName()
    {
        return name;
    }

    public String getHygieneRating()
    {
        return String.valueOf(hygieneRating);
    }

    public void setName(String n)
    {
        name = n;
    }

    public void setHygieneRating( int r)
    {
        hygieneRating = r;
    }
}
