# minesweeper-cljs

[Try it online!](https://lucascb.github.io/projects/minesweeper-cljs/index.html)

A simple Minesweeper game made using clojurescript + re-frame (and Reagent) + shadow-cljs

https://user-images.githubusercontent.com/11011361/191140108-422e8a5b-f9f3-4444-8aa0-acb703414759.mov

## Project Overview

- Architecture:
  [Single Page Application (SPA)](https://en.wikipedia.org/wiki/Single-page_application)
- Languages
  - Front end is [ClojureScript](https://clojurescript.org/) with ([re-frame](https://github.com/day8/re-frame))
- Dependencies
  - UI framework: [re-frame](https://github.com/day8/re-frame)
    ([docs](https://github.com/day8/re-frame/blob/master/docs/README.md),
    [FAQs](https://github.com/day8/re-frame/blob/master/docs/FAQs/README.md)) ->
    [Reagent](https://github.com/reagent-project/reagent) ->
    [React](https://github.com/facebook/react)
- Build tools
  - CLJS compilation, dependency management, REPL, & hot reload: [`shadow-cljs`](https://github.com/thheller/shadow-cljs)
- Development tools
  - Debugging: [CLJS DevTools](https://github.com/binaryage/cljs-devtools)

## Instructions

Clone this repository then install the dependencies using npm

```
npm install
```

Use shadow-cljs to start the local dev server

```
npm start
```

Go to http://localhost:8080/ and start playing! 🚩

## Release

To generate the static files:

```
npm run build
```
