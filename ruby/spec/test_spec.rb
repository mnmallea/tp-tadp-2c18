describe Object do

  describe '#Pruebas tp' do
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

    end
  end


end