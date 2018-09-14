module Combinators
  # 2da Parte
  def and(*matchers)
    proc do |un_obj|
      matchers.all? {|matcher| matcher.call(un_obj)} ? call(un_obj) : false
    end
  end

  def or(*matchers)
    proc do |un_obj|
      matchers.any? {|matcher| matcher.call(un_obj)} ? true : call(un_obj)
    end
  end

  def not
    proc {|un_obj| !call(un_obj)}
  end
end