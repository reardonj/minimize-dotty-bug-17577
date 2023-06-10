## Demonstratation for Dotty Bug #17577

This repository demonstrates the expontential blowup in scaladoc generation with a trait heirarchy with many nested types.

Running `sbt doc` on the repository as is should take several minutes. Moving the traits nested inside `trait A` to the top level will reduce the time to run `sbt doc` to seconds. Similarly, reducing the depth of the inheritance tree, or removing some nested types will incrementally reduce doc build time.
