require_relative '../lib/matcher'
require_relative '../lib/combinators'
require_relative '../lib/pattern'
require_relative '../lib/matches'

class Object
  include Matcher
  include Pattern
  include Matches
end

class Proc
  include Combinators
end

class Array
  def son_n_iguales?(otro_array)
    son_iguales?(otro_array.first(self.length))
  end

  def son_iguales?(otro_array)
    self.length == otro_array.length && self.cada_par_de_elementos_son_iguales(otro_array)
  end

  def cada_par_de_elementos_son_iguales(otro_array)
    self.map.with_index {
        |obj, i| type(Symbol).call(self[i]) ? obj.call(obj) : val(obj).call(otro_array[i])
    }.all?
  end
end

class Symbol
  def call(algo)
    true
  end
end