(ns spacewar.ui.protocol-test
  (:require [midje.sweet :refer :all]
            [spacewar.ui.protocols :refer :all]))
(deftype mock-drawable [state]
  Drawable
  (draw [_])
  (setup [this] this)
  (update-state [_] [(assoc state :updated true)
                     [{:event state}]])
  (get-state [_] state))

(facts
  "update-elements"
  (fact
    "degenerate"
    (update-elements {}) => [{} []])
  (fact
    "no elements"
    (update-elements {:x 1}) => [{:x 1} []])
  (fact
    "one element"
    (let [mock (->mock-drawable {:y 1})
          state {:x 1 :element mock :elements [:element]}
          [new-state events] (update-elements state)]
      new-state => {
                    :x 1
                    :element {:y 1 :updated true}
                    :elements [:element]}
      events => [{:event {:y 1}}]))
  (fact
    "two elements"
    (let [mock1 (->mock-drawable {:y 1})
          mock2 (->mock-drawable {:y 2})
          state {:x 1
                 :element1 mock1
                 :element2 mock2
                 :elements [:element1 :element2]}
          [new-state events] (update-elements state)]
      new-state => {
                    :x 1
                    :element1 {:y 1 :updated true}
                    :element2 {:y 2 :updated true}
                    :elements [:element1 :element2]}
      events => [{:event {:y 1}}
                 {:event {:y 2}}]))
  )
