public class Line
{
    public Point a;
    public Point b;

    public Line(Point a, Point b)
    {
        this.a = a;
        this.b = b;
    }

    public Line(float ax, float ay, float bx, float by)
    {
        this.a = new Point(ax, ay);
        this.b = new Point(bx, by);
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
        return (ccw(a, c, d) != ccw(b, c, d) && ccw(a, b, c) != ccw(a, b, d));
    }

    public Line getScreenPos(Camera cam)
    {
        return new Line(
            this.a.getScreenPos(cam),
            this.b.getScreenPos(cam)
        );
    }
}