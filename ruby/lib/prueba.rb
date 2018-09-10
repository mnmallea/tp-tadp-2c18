class Prueba

end

class Object
  # 1era Parte
  def val(un_obj)
    Proc.new {|otro_obj| un_obj == otro_obj}
  end

  def type(un_tipo)
    Proc.new {|un_obj| un_obj.is_a?(un_tipo)}
  end

  def list(un_array, match_size=true)
    Proc.new {|otro_array| match_size ? un_array.son_iguales?(otro_array) : un_array.son_n_iguales?(otro_array)}
  end

  def duck(symbol, *symbols)
    Proc.new do |un_obj|
      un_obj.respond_to?(symbol) && (symbols.empty? ? true : symbols.all? {|sym| un_obj.respond_to?(sym)})
    end
  end

  def with(*matchers) #No hace falta pasar el bloque como parametro aca
    Proc.new do |obj|
      if (matchers.all? {|m| m.call(obj)})
        yield
      end
    end
  end

end

class Proc
  # 2da Parte
  def and(*matchers)
    Proc.new {|un_obj| matchers.all? {|matcher| matcher.call(un_obj)} ? call(un_obj) : false}
  end

  def or(*matchers)
    Proc.new {|un_obj| matchers.any? {|matcher| matcher.call(un_obj)} ? true : call(un_obj)}
  end

  def not
    Proc.new {|un_obj| !call(un_obj)}
  end
end

class Array
  def son_n_iguales?(otro_array)
    son_iguales?(otro_array.first(self.length))
  end

  def son_iguales?(otro_array)
    self.length == otro_array.length && self.map.with_index {
    |obj, i| type(Symbol).call(self[i]) ? obj.call(obj) : val(obj).call(otro_array[i])
    }.all?
  end
end

class Symbol
  def call(algo)
    binding.local_variable_set(self, algo)
    true
  end
end