<h1 align="center"> Java-Tilemap-Editor </h1> <br>
<p align="center">
  <a href="https://github.com/Ktar5/Java-Tilemap-Editor">
    <img alt="Header" title="Header" src="https://i.imgur.com/rqWMZLc.gif">
  </a>
</p>
<div align="center">
  <strong>A tilemap editor made in Java to support customizability for your niche situations where Tiled just doesn't cut it</strong>
</div>

To create your own type of tilemap:

First, need to create a new implementation of BaseTilemap.
An example implementation can be found at WholeTilemap.
Note that both constructors are implemented.. this is needed for the tilemap to load properly.
Also note that the methods from Interactable are implemented.

If you want to create your own tileset loading procedure or modify the tileset format in any way, youre going
to need to create your own implementation of BaseTileset. Otherwise, simply use WholeTileset.

Register your tilemap and tileset (if custom) to their respective managers.



Please feel free to create an issue for any feature requests you may have!

<br></br>
Carter Gale – [@Ktar5](https://twitter.com/ktar5) – ktarfive@gmail.com – [Github](https://github.com/ktar5/)
