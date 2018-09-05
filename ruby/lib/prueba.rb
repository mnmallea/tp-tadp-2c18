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
    Proc.new {|otro_array| match_size ? val(otro_array).call(un_array) : un_array.son_n_iguales?(otro_array)}
  end

  def entiende_mensaje(sym)
    self.public_methods.include?(sym)
  end

  def duck(symbol, *symbols)
    Proc.new do |un_obj|
      un_obj.entiende_mensaje(symbol) && (symbols.empty? ? true : symbols.any? {|sym| un_obj.entiende_mensaje(sym)})
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
    self == otro_array.first(self.length)
  end
end