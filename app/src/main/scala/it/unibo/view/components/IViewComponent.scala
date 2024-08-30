package it.unibo.view.components

trait IViewComponent extends IHaveView with ICanBeDisabled
 
trait IGridComponent extends IViewComponent

trait ISidebarComponent extends IViewComponent

trait IHandComponent extends IViewComponent