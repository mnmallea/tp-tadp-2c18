describe Object do

  describe 'Tests 1era parte' do
    it 'paso test de variable' do
      expect(:a_variable_name.call('anything')).to be true
    end

    it 'paso test de val' do
      expect(val(5).call(5)).to be true
      expect(val(5).call('5')).to be false
      expect(val(5).call(4)).to be false
    end

    it 'paso test de type' do
      expect(type(Integer).call(5)).to be true
      expect(type(Symbol).call('Trust me, sarasa')).to be false
      expect(type(Symbol).call(:a_true_symbol)).to be true
    end

    it 'paso test de list' do
      an_array = [1, 2, 3, 4]

      expect(list([1, 2, 3, 4], true).call(an_array)).to be true
      expect(list([1, 2, 3, 4], false).call(an_array)).to be true

      expect(list([1, 2, 3], true).call(an_array)).to be false
      expect(list([1, 2, 3], false).call(an_array)).to be true

      expect(list([2, 1, 3, 4], true).call(an_array)).to be false
      expect(list([2, 1, 3, 4], true).call(an_array)).to be false

      expect(list([1, 2, 3]).call(an_array)).to be false

      expect(list([:a, :b, :c, :d]).call(an_array)).to be true
    end

    it 'paso test de duck' do

      psyduck = Object.new
      def psyduck.cuack
        'psy..duck?'
      end
      def psyduck.fly
        '(headache)'
      end

      class Dragon
        def fly
          'do some flying'
        end
      end
      a_dragon = Dragon.new

      expect(duck(:cuack, :fly).call(psyduck)).to be true
      expect(duck(:cuack, :fly).call(a_dragon)).to be false
      expect(duck(:fly).call(a_dragon)).to be true
      expect(duck(:to_s).call(Object.new)).to be true

      expect(duck(:hace_tu_gracia).call(psyduck)).to be false

    end
  end

  describe 'Tests 2da parte' do
    it 'paso tests de and' do
      expect(duck(:+).and(type(Fixnum), val(5)).call(5)).to be true
      expect(duck(:hola).and(type(Fixnum), val(5)).call(5)).to be false
      expect(duck(:+).and(type(Fixnum), val(8)).call(5)).to be false
    end

    it 'paso tests de or' do
      expect(duck(:+).or(duck(:hola), val(4)).call(5)).to be true
      expect(duck(:hola).or(duck(:+), val(4)).call(5)).to be true
      expect(duck(:hola).or(duck(:otro_sym), val(4)).call(5)).to be false
    end

    it 'paso tests de not' do
      expect(type(Integer).not.call(5)).to be false
      expect(duck(:hola).not.call(4)).to be true
    end
  end

  describe 'Tests 3era parte' do
    it 'paso tests de with' do
      #expect(with(type(String), :a_string) { a_string.length }.call("Hola")).to be 4
    end
  end


end