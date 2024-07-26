package it.unibo.model.gameboard.grid

/** Utility for handling given instances. */
object GivenExtension:
  /**
   * Creates a context with the specified given instance.
   * @param givenInstance the specified given instance
   * @param context the created context where the given instance is made available
   * @tparam A the type of the given instance
   * @tparam B the type of the result of the configuration
   * @return the result of the configuration
   */
  def within[A, B](givenInstance: A)(context: A ?=> B): B =
    given A = givenInstance
    context