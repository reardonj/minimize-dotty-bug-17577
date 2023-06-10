object Concrete extends T
// object DoesNotTerminate extends X


trait A {
  trait AA
  trait BB
  trait CC
  trait DD
  trait EE
  trait FF
  trait GG
  trait HH
  trait II
  trait JJ
  trait AA_1
  trait BB_1
  trait CC_1
  trait DD_1
  trait EE_1
  trait FF_1
  trait GG_1
  trait HH_1
  trait II_1
  trait JJ_1
  trait AA_2
  trait BB_2
  trait CC_2
  trait DD_2
  trait EE_2
  trait FF_2
  trait GG_2
  trait HH_2
  trait II_2
  trait JJ_2

}

trait B extends A {
  trait B_AA extends AA
  trait B_BB extends BB
  trait B_CC extends CC
  trait B_DD extends DD
  trait B_EE extends EE
  trait B_FF extends FF
  trait B_GG extends GG
  trait B_HH extends HH
  trait B_II extends II
  trait B_JJ extends JJ
  trait B_AA_1 extends AA_1
  trait B_BB_1 extends BB_1
  trait B_CC_1 extends CC_1
  trait B_DD_1 extends DD_1
  trait B_EE_1 extends EE_1
  trait B_FF_1 extends FF_1
  trait B_GG_1 extends GG_1
  trait B_HH_1 extends HH_1
  trait B_II_1 extends II_1
  trait B_JJ_1 extends JJ_1
}

trait C extends B {
  trait C_AA extends B_AA 
  trait C_BB extends B_BB 
  trait C_CC extends B_CC 
  trait C_DD extends B_DD 
  trait C_EE extends B_EE 
  trait C_FF extends B_FF 
  trait C_GG extends B_GG 
  trait C_HH extends B_HH 
  trait C_II extends B_II 
  trait C_JJ extends B_JJ 
  trait C_AA_1 extends B_AA_1
  trait C_BB_1 extends B_BB_1
  trait C_CC_1 extends B_CC_1
  trait C_DD_1 extends B_DD_1
  trait C_EE_1 extends B_EE_1
  trait C_FF_1 extends B_FF_1
  trait C_GG_1 extends B_GG_1
  trait C_HH_1 extends B_HH_1
  trait C_II_1 extends B_II_1
  trait C_JJ_1 extends B_JJ_1
}

trait D extends C {
  trait D_AA extends C_AA
  trait D_BB extends C_BB
  trait D_CC extends C_CC
  trait D_DD extends C_DD
  trait D_EE extends C_EE
  trait D_FF extends C_FF
  trait D_GG extends C_GG
  trait D_HH extends C_HH
  trait D_II extends C_II
  trait D_JJ extends C_JJ
}

trait E extends D {
  case object E_AA extends D_AA
  case object E_BB extends D_BB
  case object E_CC extends D_CC
  case object E_DD extends D_DD
  case object E_EE extends D_EE
  case object E_FF extends D_FF
  case object E_GG extends D_GG
  case object E_HH extends D_HH
  case object E_II extends D_II
  case object E_JJ extends D_JJ
}


trait F extends E
trait G extends F
trait H extends G
trait I extends H
trait J extends I
trait K extends J
trait L extends K
trait M extends L
trait N extends M
trait O extends N
trait P extends O
trait Q extends P
trait R extends Q
trait S extends R
trait T extends S



