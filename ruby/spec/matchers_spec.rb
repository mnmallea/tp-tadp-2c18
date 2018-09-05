require 'rspec'
require_relative '../lib/matcher'

describe 'Tests para matchers' do

  let :duck do
    pato = Object.new
    pato.send :define_singleton_method, :cuack do 'cuack cuack' end
    pato.send :define_singleton_method, :fly do 'I believe I can fly' end
    pato
  end

  it 'duck matchea con cuack y fly' do
    matcher = MatcherFactory.duck :cuack, :fly
    expect(matcher.call(duck)).to be true
  end

  it 'duck matchea con cuack' do
    matcher = MatcherFactory.duck :cuack
    expect(matcher.call(duck)).to be true
  end

  it 'duck matchea con ningun mensaje' do
    matcher = MatcherFactory.duck
    expect(matcher.call(duck)).to be true
  end

  it 'duck no matchea con nadar' do
    matcher = MatcherFactory.duck :nadar
    expect(matcher.call(duck)).to be false
  end
end