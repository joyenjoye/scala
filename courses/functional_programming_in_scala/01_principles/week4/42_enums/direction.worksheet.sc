enum Direction(val dx: Int, val dy: Int):
  case Right extends Direction( 1, 0)
  case Up extends Direction( 0, 1)
  case Left extends Direction(-1, 0)
  case Down extends Direction( 0, -1)
  def leftTurn = Direction.values((ordinal + 1) % 4)
end Direction

val r = Direction.Down
val u = r.leftTurn 
val v = (u.dx, u.dy)
val x = Direction.Right.ordinal
val y = Direction.Up.ordinal
val z = Direction.Left.ordinal