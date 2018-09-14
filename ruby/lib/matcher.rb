module Matcher
  # 1era Parte
  def val(un_obj)
    proc {|otro_obj| un_obj == otro_obj}
  end

  def type(un_tipo)
    proc {|un_obj| un_obj.is_a?(un_tipo)}
  end

  def list(un_array, match_size=true)
    proc do |otro_array|
      match_size ? un_array.son_iguales?(otro_array) : un_array.son_n_iguales?(otro_array)
    end
  end

  def duck(symbol, *symbols)
    proc do |un_obj|
      un_obj.respond_to?(symbol) && (symbols.empty? ? true : symbols.all? {|sym| un_obj.respond_to?(sym)})
    end
  end
end

