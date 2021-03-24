package com.belvin.stem

import java.math.BigInteger

class Rational(var n:BigInteger,var d:BigInteger):Comparable<Rational>
{
    init
    {
        if(d<0.toBigInteger())
        {
            n *= (-1).toBigInteger()
            d *= (-1).toBigInteger()
        }
        val gcd= n.gcd(d)
        n /= gcd
        d /= gcd
    }

    override fun toString():String
    {
        if(this.d == 1.toBigInteger())
        {
            return "${this.n}"
        }
        return "${this.n}/${this.d}"
    }

    override fun compareTo(other:Rational):Int
    {
        return (this.n.toDouble()/this.d.toDouble()).compareTo(other.n.toDouble()/other.d.toDouble())
    }

    override fun equals(other:Any?):Boolean
    {

        if(other is Rational)
        {
            return (this.n == other.n) && (other.d == other.d)
        }
        throw Exception("Cant Compare")

    }

    override fun hashCode(): Int {
        var result = n.hashCode()
        result = 31 * result + d.hashCode()
        return result
    }
}

infix fun Number.divBy(other: Number): Rational
{
    return Rational(this.toLong().toBigInteger(),other.toLong().toBigInteger())
}


fun <T> T.toRational():Rational
{
    if (this is String)
    {
        if(this.split("/").size == 1)
        {
            return Rational(this.toBigInteger(),1.toBigInteger())
        }
        if(this.split("/")[1] == "0")
        {
            throw Exception("IllegalArgumentException")
        }
        return Rational(this.split("/")[0].toBigInteger(),
            this.split("/")[1].toBigInteger())
    }
    return Rational(this as BigInteger,1.toBigInteger())
}

operator fun Rational.plus(other:Rational):Rational
{
    val n = (this.n * other.d) + (this.d * other.n)
    return Rational(n,this.d*other.d)
}

operator fun Rational.minus(other:Rational):Rational
{
    val n = (this.n * other.d) - (this.d * other.n)
    return Rational(n,this.d*other.d)
}

operator fun Rational.times(other:Rational):Rational
{
    return Rational(this.n * other.n,this.d*other.d)
}

operator fun Rational.div(other:Rational):Rational
{
    return Rational(this.n*other.d,other.n*this.d)
}

operator fun Rational.unaryMinus():Rational
{
    return Rational(this.n * (-1).toBigInteger(),this.d)
}

operator fun Rational.rangeTo(other:Rational):Pair<Rational,Rational>
{

    var fn = this.n
    var fd = this.d
    var sn = other.n
    var sd = other.d

    if(this.d != other.d)
    {
        fn = fn*sd
        sn = sn*fd
        sd = sd*fd
        fd = sd
    }

    val fv = fn
    while(fn <= sn)
    {
        fn++
    }
    val lv = fn

    return Pair(Rational(fv,sd) , Rational(lv,sd))
}

operator fun Pair<Rational,Rational>.contains(other: Rational):Boolean
{
    return other.n.toDouble()/other.d.toDouble() >= first.n.toDouble()/first.d.toDouble() && other.n.toDouble()/other.d.toDouble() <= second.n.toDouble()/second.d.toDouble()
}