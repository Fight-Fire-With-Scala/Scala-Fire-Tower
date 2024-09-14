package it.unibo.view.component

trait IViewComponent extends IHaveView with ICanBeDisabled

trait IGridComponent extends IViewComponent

trait ISidebarComponent extends IViewComponent

trait IHandComponent extends IViewComponent
