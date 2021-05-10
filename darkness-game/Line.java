public class Line
{
    public Point a;
    public Point b;

    public Line(Point a, Point b)
    {
        this.a = a;
        this.b = b;
    }

    private boolean ccw(Point a, Point b, Point c)
    {
        return (c.y-a.y) * (b.x-a.x) > (b.y-a.y) * (c.x-a.x);
    }

    public boolean intersects(Line line2)
    {
        Point a = this.a;
        Point b = this.b;
        Point c = line2.a;
        Point d = line2.b;
        if (ccw(a, c, d) != ccw(b, c, d) && ccw(a, b, c) != ccw(a, b, d)) {
            return true;
        }
        return false;
    }
}