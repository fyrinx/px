import {Show} from "../../model/api/show";
import axios from 'axios';
import { Component, createSignal } from "solid-js";

const Summary: Component= () => {
  const [textStore,setText] = createSignal<string>(); 
  async function getTopTen(){
    try {
      const {data,status}= await axios.get<Show[]>(
        'api/getAllShow',
        {
          headers: {
            Accept: 'application/json',
          },
        },
      );
    
    
    console.log(data);
    // 👇️ "response status is: 200"
    getString(data);
    }catch (error) {
      if (axios.isAxiosError(error)) {
        console.log('error message: ', error.message);
        return error.message;
      } else {
        console.log('unexpected error: ', error);
        return 'An unexpected error occurred';
      }
    }
    console.log(textStore());
  }
  function getString(data : Show[]){
    var output:string="SHOW_NAME;NETWORK;EPISODE_COUNT;RELEASED_EPISODE_COUNT\n";
    if (data?.length !== 0){
      for(let i=0;i<data.length;i++){
        output=output.concat(data[i].showName+";"+data[i].networkName+";"+data[i].episodeCount+";"+data[i].releasedEpisodeCount+"\n");
      }
    }
    console.log(output);
    setText(output);

  }

  function download(){
    const a = document.createElement('a') // Create "a" element
    const blob = new Blob([textStore() as string], {type: "text/plain"}) // Create a blob (file-like object)
    const url = URL.createObjectURL(blob) // Create an object URL from blob
    a.setAttribute('href', url) // Set "a" element link
    a.setAttribute('download', "summary") // Set download filename
    a.click() // Start downloading
  }
  return (
    <div>

        <button onClick={getTopTen}> Get Summary!</button>
        <button onClick={download} >Download Summary</button>
    </div>
  )


}
export default Summary;
