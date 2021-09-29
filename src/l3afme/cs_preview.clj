(ns l3afme.cs-preview
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.awt.image BufferedImage]
           [java.awt Color]
           [javax.imageio ImageIO]))

(defn get-colors
  "Get colors from file contents"
  [file-contents]
  (map (fn [col] (Color/decode col)) (str/split (nth (str/split-lines file-contents) 0) #" ")))

(defn get-code-lines
  "Get code lines from file contents"
  [file-contents]
  (map #(str/split % #"")
       (nth (split-at 2 (str/split-lines file-contents)) 1)))

(defn with-idx
  "Map seq items to index"
  [seq]
  (map #(vector %2 %1) seq (range)))

(defn is-valid-char
  "Check char is valid"
  [char max-color]
  (if (not= char " ")
    (try
      (let [i (Integer/parseInt char)]
        (> i max-color))
      (catch Exception _
        true))
    false))

(defn check-file-contents
  "Check file contents to ensure all colors are present"
  [file-contents]
  (let [max-color (- (count (get-colors file-contents)) 1)
        code-lines (with-idx (get-code-lines file-contents))
        mapped (map (fn [line]
                      [(first line)
                       (with-idx (second line))])
                 code-lines)]
    (filter (fn [line] (not-empty (second line)))
            (map (fn [line]
                   [(first line)
                    (filter (fn [char]
                             (is-valid-char (second char) max-color))
                           (second line))])
                 mapped))))

(defn print-invalid-contents
  "Print invalid contents and positions"
  [contents]
  (let [messages
            (mapcat (fn [line]
                     (map (fn [char]
                           (format "Line: %d Pos: %d Character: \"%s\"" (first line) (first char) (second char)))
                          (second line)))
             contents)]
    (println "Invalid characters found:")
    (println (str/join "\n" messages))))

(def corner-radius 16)
(def char-height   24)
(def char-width    16)
(def line-padding  12)
(def padding       32)

(defn gen-test-image
  [input]
  (let [colors (get-colors input)
        raw-lines (get-code-lines input)
        lines (with-idx (map with-idx raw-lines))
        longest (count (reduce #(if (> (count %1) (count %2)) %1 %2) raw-lines))
        width (+ (* longest char-width) (* padding 2))
        height (+ (- (* (count lines) (+ char-height line-padding)) line-padding) (* padding 2))
        bi (BufferedImage. width height BufferedImage/TYPE_INT_RGB)
        g (.createGraphics bi)]
    (.setColor g (nth colors 0))
    (.fillRect g 0 0 width height)

    (doseq [line lines]
      (doseq [char (second line)]
        (let [c (second char)
              line-y (first line)
              char-x (first char)
              y (+ padding (* line-y (+ char-height line-padding)))
              x (+ padding (* char-x char-width))]
          (when (not= c " ")
            (.setColor g (nth colors (Integer/parseInt c)))
            (.fillRect g x y char-width char-height)))))

    (let [f (io/file "./output.jpg")]
      (ImageIO/write bi "JPG" f))
    (println width "x" height)))

(defn -main
  "Start stuff"
  [& args]
  (let [path (first args)
        file (io/file path)]
    (if (not (.isFile file))
      (println (format "Invalid file '%s'" path))
      (let [file-contents (slurp path)
            invalid (check-file-contents file-contents)]
        (if (not= 0 (count invalid))
          (print-invalid-contents invalid)
          (gen-test-image file-contents))))))

