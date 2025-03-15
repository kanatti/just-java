alias gw="./gradlew"
alias run-netty-server="gw runMain -PmainClass=com.kanatti.netty.EchoServerApp"
alias run-netty-client="gw runMain -PmainClass=com.kanatti.netty.EchoClientApp"


function gw-run() {
  if [ -z "$1" ]; then
    echo "Usage: gw-run <MainClassSuffix>"
    return 1
  fi

  gw --warning-mode=none runMain -PmainClass=com.kanatti."$1"
}